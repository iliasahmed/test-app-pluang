package com.iliasahmed.testpluang.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.iliasahmed.testpluang.controller.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {
        if (inputFormat.equals("")) { // if inputFormat = "", set a default input format.
            if (inputDate.contains("."))
                inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            else
                inputFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        if (outputFormat.equals("")) {
            outputFormat = "EEEE d',' MMMM  yyyy"; // if inputFormat = "", set a default output format.
        }
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);

        df_input.setTimeZone(TimeZone.getTimeZone("gmt"));

        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);
        df_output.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;
    }

    public static String getName(String sid){
        return AppController.nameMap.get(sid) == null ? "NOT FOUND" : AppController.nameMap.get(sid);
    }
}
