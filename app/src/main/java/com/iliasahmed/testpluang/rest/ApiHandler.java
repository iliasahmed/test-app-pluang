package com.iliasahmed.testpluang.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iliasahmed.testpluang.R;
import com.iliasahmed.testpluang.controller.AppController;
import com.iliasahmed.testpluang.listener.ResponseListener;
import com.iliasahmed.testpluang.model.CommonSuccessResponse;
import com.iliasahmed.testpluang.utils.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

@Singleton
public class ApiHandler {

    @SuppressWarnings("unchecked")
    public <T, V> void createCall(Call<T> call, ResponseListener<T, V> responseListener) {
        if (!Utils.isNetworkAvailable(AppController.getContext())) {
            responseListener.onFailed((V) AppController.getContext().getString(R.string.networkError), 500);
            return;
        }

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response != null && responseListener != null)
                    successProcessor(call, response, responseListener);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                responseListener.onFailed((V) getCustomErrorMessage(t), 0);
            }
        });
    }

    private String getCustomErrorMessage(Throwable error) {
        if (error instanceof SocketTimeoutException) {
            return AppController.getContext().getString(R.string.requestTimeOutError);
        } else if (error instanceof IOException) {
            return AppController.getContext().getString(R.string.networkError);
        } else if (error instanceof HttpException) {
            return (((HttpException) error).response().message());
        } else {
            return AppController.getContext().getString(R.string.unknownError);
        }
    }

    @SuppressWarnings("unchecked")
    private <T, V> void successProcessor(Call<T> call, Response<T> response, ResponseListener<T, V> responseListener) {
        int statusCode = response.code();
        if (statusCode >= 200 && statusCode < 400) {
            if (response.body() != null) {
                if (response.body().getClass().isAssignableFrom(CommonSuccessResponse.class)) {
                    CommonSuccessResponse<T> commonSuccessResponse = (CommonSuccessResponse<T>) response.body();
                    if (commonSuccessResponse != null) {
                        responseListener.onDataFetched(response.body(), response.code());
                    } else {
                        responseListener.onFailed((V) (AppController.getContext().getResources().getString(R.string.unknownError)), statusCode);
                    }
                } else {
                    responseListener.onDataFetched(response.body(), statusCode);
                }
            } else {
                responseListener.onDataFetched(response.body(), statusCode);
            }
        } else {
            try {
                Gson gson = new Gson();
                if (response.errorBody() != null) {
                    try {
                        String error = null;
                        CommonSuccessResponse res = gson.fromJson(response.errorBody().string(), CommonSuccessResponse.class);
                        if (res != null) {
                            error = AppController.getContext().getResources().getString(R.string.unknownError);
                        }
                        if (error != null) {
                            responseListener.onFailed((V) error, statusCode);
                        } else {
                            responseListener.onFailed((V) AppController.getContext().getString(R.string.unknownError), statusCode);
                        }
                    } catch (JsonSyntaxException ex) {
                        responseListener.onFailed((V) AppController.getContext().getString(R.string.unknownError), statusCode);
                        ex.printStackTrace();
                    }
                }
            } catch (IOException e) {
                responseListener.onFailed((V) AppController.getContext().getString(R.string.unknownError), statusCode);
                e.printStackTrace();
            }
        }
    }
}
