package com.iliasahmed.testpluang.rest;

public class ApiServiceHolder {

    private IApiClient apiService;

    public IApiClient getApiService() {
        return apiService;
    }

    public void setApiService(IApiClient apiService) {
        this.apiService = apiService;
    }
}
