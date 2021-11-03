package com.iliasahmed.testpluang.controller;


import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.iliasahmed.testpluang.data.PreferenceRepository;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class AppController extends Application {

    public static AppController mAppController;
    public static Context mContext;
    private static AppEvent appEvent;
    public static HashMap<String, String> nameMap = new HashMap<>();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    PreferenceRepository preferenceRepository;

    public static synchronized void setAppEvent(AppEvent event) {
        appEvent = event;
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized AppController getInstance() {
        return mAppController;
    }

    public static synchronized void onLogoutEvent() {
        if (appEvent != null) appEvent.onLogout();
    }

    public PreferenceRepository getPreferenceRepository() {
        return preferenceRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppController = this;
        mContext = getApplicationContext();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        nameMap.put("RELI","RELIANCE INDUSTRIES");
        nameMap.put("TCS","TATA CONSULTANCY SERVICES");
        nameMap.put("ITC","ITC Ltd");
        nameMap.put("HDBK","HDFC BANK");
        nameMap.put("INFY","INFOSYS");
    }

    public interface AppEvent {
        void onLogout();
    }
}
