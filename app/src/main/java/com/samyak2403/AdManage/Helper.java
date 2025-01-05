package com.samyak2403.AdManage;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.samyak2403.ghostdetecter.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Helper {
    private final Activity activity;
    private InterstitialAd mInterstitialAd;
    private int countClicks = 0;

    public Helper(Activity activity) {
        this.activity = activity;
        initializeAdMob();
    }

    // Initialize Mobile Ads SDK
    private void initializeAdMob() {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d("Helper", "AdMob initialized.");
                prepareInterstitialAd();
            }
        });
    }

    // Load Interstitial Ad
    private void prepareInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, activity.getString(R.string.admob_interstitial_id),
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.d("Helper", "Interstitial ad loaded.");
                        setFullScreenContentCallback();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("Helper", "Failed to load interstitial ad: " + loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    // Set Callbacks for Interstitial Ad
    private void setFullScreenContentCallback() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    Log.d("Helper", "Ad clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d("Helper", "Ad dismissed.");
                    mInterstitialAd = null;
                    prepareInterstitialAd(); // Reload ad after dismissal
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e("Helper", "Failed to show interstitial ad: " + adError.getMessage());
                    mInterstitialAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d("Helper", "Ad showed.");
                    mInterstitialAd = null;
                }
            });
        }
    }

    // Show Interstitial Ad
    public void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            Log.w("Helper", "Ad is not ready to be shown.");
        }
    }

    // Show Ad Based on Click Count
    public void showCounterInterstitialAd(int threshold) {
        countClicks++;
        if (countClicks >= threshold) {
            showInterstitialAd();
            countClicks = 0; // Reset counter after showing ad
        }
    }
}
