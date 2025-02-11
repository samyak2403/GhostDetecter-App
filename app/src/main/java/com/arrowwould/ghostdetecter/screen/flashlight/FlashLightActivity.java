package com.arrowwould.ghostdetecter.screen.flashlight;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arrowwould.ghostdetecter.databinding.ActivityFlashLightBinding;
import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.R;
import com.arrowwould.ghostdetecter.screen.MainActivity;

public class FlashLightActivity extends AppCompatActivity {
    private static final String TAG = "FlashLightActivity";

    private boolean isFlashOn = false;
    private CameraManager cameraManager;
    private String cameraId;
    private ActivityFlashLightBinding binding;

    private Helper adHelper;
    private Control adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Initialize ViewBinding
            binding = ActivityFlashLightBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Ad components
            initializeAdComponents();

            // Setup Toolbar
            setupToolbar();

            // Check and setup flashlight
            setupFlashlight();

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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Flash Light");
        }
    }

    private void setupFlashlight() {
        // Check if the device has a flashlight
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            showFlashlightUnavailableError();
            return;
        }

        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            
            // Get the first camera ID (usually rear camera)
            String[] cameraIds = cameraManager.getCameraIdList();
            if (cameraIds.length > 0) {
                cameraId = cameraIds[0];
            } else {
                showFlashlightUnavailableError();
                return;
            }

            // Set up click listener for the flashlight toggle
            binding.flashImageView.setOnClickListener(v -> toggleFlashlight());

            // Initial icon state
            updateFlashIcon();

        } catch (CameraAccessException e) {
            Log.e(TAG, "Camera access error", e);
            showFlashlightUnavailableError();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error setting up flashlight", e);
            showFlashlightUnavailableError();
        }
    }

    private void toggleFlashlight() {
        if (cameraManager == null || cameraId == null) {
            showFlashlightUnavailableError();
            return;
        }

        try {
            isFlashOn = !isFlashOn;
            cameraManager.setTorchMode(cameraId, isFlashOn);
            updateFlashIcon();
            
            // Provide user feedback
            Toast.makeText(this, isFlashOn ? "Flashlight ON" : "Flashlight OFF", 
                Toast.LENGTH_SHORT).show();
        } catch (CameraAccessException e) {
            Log.e(TAG, "Error toggling flashlight", e);
            Toast.makeText(this, "Unable to control flashlight", Toast.LENGTH_SHORT).show();
            isFlashOn = false;
            updateFlashIcon();
        }
    }

    private void updateFlashIcon() {
        // Change the icon based on the flashlight state
        binding.flashImageView.setImageResource(
            isFlashOn ? R.drawable.power_on : R.drawable.power_off
        );
    }

    private void showFlashlightUnavailableError() {
        Toast.makeText(this, "Flashlight not available on this device", 
            Toast.LENGTH_LONG).show();
        binding.flashImageView.setEnabled(false);
        binding.flashImageView.setImageResource(R.drawable.power_off);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackNavigation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleBackNavigation() {
        try {
            // Turn off flashlight if it's on
            if (isFlashOn) {
                toggleFlashlight();
            }

            // Navigate back and show ad
            Intent intent = new Intent(FlashLightActivity.this, MainActivity.class);
            startActivity(intent);
            
            if (adHelper != null) {
                adHelper.showInterstitialAd();
            }
            
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Back navigation error", e);
            Toast.makeText(this, "Navigation error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Ensure flashlight is turned off when activity is paused
        if (isFlashOn) {
            toggleFlashlight();
        }
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

    private void showErrorAndExit(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        finish();
    }
}
