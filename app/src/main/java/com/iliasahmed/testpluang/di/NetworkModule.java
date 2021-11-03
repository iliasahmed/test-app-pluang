package com.iliasahmed.testpluang.di;


import com.iliasahmed.testpluang.data.PreferenceRepository;
import com.iliasahmed.testpluang.rest.ApiHandler;
import com.iliasahmed.testpluang.rest.ApiRepository;
import com.iliasahmed.testpluang.rest.ApiServiceHolder;
import com.iliasahmed.testpluang.rest.IApiClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    ApiRepository provideApiRepository(IApiClient apiService, PreferenceRepository preferenceRepository, ApiHandler apiHandler) {
        return new ApiRepository(apiService, preferenceRepository, apiHandler);
    }

    @Provides
    @Singleton
    ApiServiceHolder apiServiceHolder() {
        return new ApiServiceHolder();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(PreferenceRepository preferenceRepository) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(120, TimeUnit.SECONDS);
        okHttpClient.readTimeout(120, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(120, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    IApiClient provideApiService(Retrofit retrofit, ApiServiceHolder apiServiceHolder) {
        IApiClient client = retrofit.create(IApiClient.class);
        apiServiceHolder.setApiService(client);
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://api.tickertape.in/stocks/quotes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiHandler provideApiHandler() {
        return new ApiHandler();
    }

}
