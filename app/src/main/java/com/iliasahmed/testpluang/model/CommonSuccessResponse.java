package com.iliasahmed.testpluang.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonSuccessResponse<T> {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName(value = "data", alternate = "results")
    @Expose
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }
}
