package com.arrowwould.ghostdetecter;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import arrowwould.in.admobopenads.AppOpenManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);

        new AppOpenManager(this, getString(R.string.open_ads));

    }

}