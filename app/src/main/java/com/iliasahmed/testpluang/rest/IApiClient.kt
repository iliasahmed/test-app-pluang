package com.iliasahmed.testpluang.rest

import com.iliasahmed.testpluang.model.CommonSuccessResponse
import com.iliasahmed.testpluang.model.QuotesModel
import com.iliasahmed.testpluang.utils.ConstantUtils
import retrofit2.Call
import retrofit2.http.GET

interface IApiClient {

    @GET(ConstantUtils.API_URL)
    fun getQuotesData(): Call<CommonSuccessResponse<List<QuotesModel?>?>?>?
}