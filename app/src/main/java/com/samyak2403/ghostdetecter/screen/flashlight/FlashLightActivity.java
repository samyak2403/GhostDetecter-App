package com.samyak2403.ghostdetecter.screen.flashlight;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arrowwould.ghostdetecter.databinding.ActivityFlashLightBinding;
import com.samyak2403.AdManage.Control;
import com.samyak2403.AdManage.Helper;
import com.samyak2403.ghostdetecter.R;

import com.samyak2403.ghostdetecter.screen.MainActivity;


public class FlashLightActivity extends AppCompatActivity {

    private boolean isFlashOn = false;
    private CameraManager cameraManager;
    private String cameraId;
    private ActivityFlashLightBinding binding;

    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityFlashLightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adHelper = new Helper(this);

        //---- banner ads start ----//
        Control control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Flash Light");
        }

        // Check if the device has a flashlight
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Flashlight not available on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Usually the rear camera
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error accessing camera", Toast.LENGTH_SHORT).show();
        }

        // Set up click listener for the flashlight toggle
        binding.flashImageView.setOnClickListener(v -> toggleFlashlight());
    }

    private void toggleFlashlight() {
        try {
            isFlashOn = !isFlashOn;
            cameraManager.setTorchMode(cameraId, isFlashOn);
            updateFlashIcon();
            Toast.makeText(this, isFlashOn ? "Flashlight ON" : "Flashlight OFF", Toast.LENGTH_SHORT).show();
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error accessing flashlight", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFlashIcon() {
        // Change the icon based on the flashlight state
        binding.flashImageView.setImageResource(
                isFlashOn ? R.drawable.power_on : R.drawable.power_off
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            Intent intent = new Intent(FlashLightActivity.this, MainActivity.class);
            startActivity(intent);
            adHelper.showInterstitialAd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
