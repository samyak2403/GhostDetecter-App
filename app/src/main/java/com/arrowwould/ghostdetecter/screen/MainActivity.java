package com.arrowwould.ghostdetecter.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arrowwould.ghostdetecter.databinding.ActivityMainBinding;
import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.GhostDetectedActivity;
import com.arrowwould.ghostdetecter.R;

import com.arrowwould.ghostdetecter.devloperProfile.DevloperProfileActivity;
import com.arrowwould.ghostdetecter.screen.Compass.CompassActivity;
import com.arrowwould.ghostdetecter.screen.demo.DemoActivity;
import com.arrowwould.ghostdetecter.screen.flashlight.FlashLightActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Declare View Binding instance
    private ActivityMainBinding binding;

    private Helper adHelper;
    private Control adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Initialize View Binding
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Ad components
            initializeAdComponents();

            // Set up button click listeners
            setupButtonListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            showErrorAndExit("Initialization failed");
        }
    }

    private void initializeAdComponents() {
        try {
            // Initialize Ad Management
            adControl = new Control(this);
            adControl.loadBannerAd(R.id.bannerLayout);

            // Initialize Helper class for Ad Management
            adHelper = new Helper(this);
        } catch (Exception e) {
            Log.e(TAG, "Ad initialization error", e);
            Toast.makeText(this, "Ad initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupButtonListeners() {
        // Set up click listeners with error handling
        setButtonListener(binding.ghostBtn, this::navigateToGhostDetector);
        setButtonListener(binding.magneticSensorBtn, this::navigateToSensorCheck);
        setButtonListener(binding.demoBtn, this::navigateToDemoActivity);
        setButtonListener(binding.aboutBtn, this::navigateToAboutActivity);
        setButtonListener(binding.CompassBtn, this::navigateToCompassActivity);
        setButtonListener(binding.moreBtn, this::navigateToFlashLightActivity);
        setButtonListener(binding.devProfile, this::navigateToDevProfile);
    }

    private void setButtonListener(View button, Runnable navigationAction) {
        button.setOnClickListener(v -> {
            try {
                navigationAction.run();
            } catch (Exception e) {
                Log.e(TAG, "Navigation error", e);
                Toast.makeText(this, "Unable to navigate", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToDevProfile() {
        navigateToActivity(DevloperProfileActivity.class, true);
    }

    private void navigateToGhostDetector() {
        navigateToActivity(GhostDetectedActivity.class, true);
        if (adHelper != null) {
            adHelper.showCounterInterstitialAd(0); // Show ad every 5 clicks
        }
    }

    private void navigateToSensorCheck() {
        navigateToActivity(SensorCheckActivity.class, false);
        if (adHelper != null) {
            adHelper.showInterstitialAd();
        }
    }

    private void navigateToDemoActivity() {
        navigateToActivity(DemoActivity.class, true);
        if (adHelper != null) {
            adHelper.showInterstitialAd();
        }
    }

    private void navigateToAboutActivity() {
        navigateToActivity(AboutActivity.class, false);
        if (adHelper != null) {
            adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks
        }
    }

    private void navigateToCompassActivity() {
        navigateToActivity(CompassActivity.class, true);
        if (adHelper != null) {
            adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks
        }
    }

    private void navigateToFlashLightActivity() {
        navigateToActivity(FlashLightActivity.class, true);
    }

    private void navigateToActivity(Class<?> activityClass, boolean shouldFinish) {
        try {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
            
            if (shouldFinish) {
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Navigation error to " + activityClass.getSimpleName(), e);
            Toast.makeText(this, "Unable to open activity", Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorAndExit(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        // Clean up resources
        if (adControl != null) {
            adControl = null;
        }
        if (adHelper != null) {
            adHelper = null;
        }
        
        // Nullify binding to prevent memory leaks
        binding = null;
        
        super.onDestroy();
    }
}