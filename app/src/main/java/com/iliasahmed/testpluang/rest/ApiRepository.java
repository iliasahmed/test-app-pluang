package com.iliasahmed.testpluang.rest;

import com.iliasahmed.testpluang.data.PreferenceRepository;
import com.iliasahmed.testpluang.listener.ResponseListener;
import com.iliasahmed.testpluang.model.CommonSuccessResponse;
import com.iliasahmed.testpluang.model.QuotesModel;

import java.util.List;

import javax.inject.Inject;

public class ApiRepository {
    private IApiClient apiService;
    private PreferenceRepository preferenceRepository;
    private ApiHandler apiHandler;

    @Inject
    public ApiRepository(IApiClient apiService,
                         PreferenceRepository preferenceRepository,
                         ApiHandler apiHandler) {
        this.apiService = apiService;
        this.preferenceRepository = preferenceRepository;
        this.apiHandler = apiHandler;
    }

    public void getQuotes(ResponseListener<CommonSuccessResponse<List<QuotesModel>>, String> listener) {
        apiHandler.createCall(apiService.getQuotesData(), listener);
    }
}
