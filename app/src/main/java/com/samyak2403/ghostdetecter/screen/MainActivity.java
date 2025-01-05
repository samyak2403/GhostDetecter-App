package com.samyak2403.ghostdetecter.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.arrowwould.ghostdetecter.databinding.ActivityMainBinding;
import com.samyak2403.AdManage.Control;
import com.samyak2403.AdManage.Helper;
import com.samyak2403.ghostdetecter.GhostDetectedActivity;
import com.samyak2403.ghostdetecter.R;

import com.samyak2403.ghostdetecter.devloperProfile.DevloperProfileActivity;
import com.samyak2403.ghostdetecter.screen.Compass.CompassActivity;
import com.samyak2403.ghostdetecter.screen.demo.DemoActivity;
import com.samyak2403.ghostdetecter.screen.flashlight.FlashLightActivity;


public class MainActivity extends AppCompatActivity {

    // Declare View Binding instance
    public ActivityMainBinding binding;

    private Intent intent;


    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up button click listeners using binding
        binding.ghostBtn.setOnClickListener(this::onGhostButtonClick);
        binding.magneticSensorBtn.setOnClickListener(this::onMagneticSensorButtonClick);
        binding.demoBtn.setOnClickListener(this::onDemoButtonClick);
        binding.aboutBtn.setOnClickListener(this::onAboutButtonClick);
        binding.CompassBtn.setOnClickListener(this::CompassButtonClick);
        binding.moreBtn.setOnClickListener(this::onMoreButton);
        binding.devProfile.setOnClickListener(this::onDevloperProfile);

        //---- banner ads start ----//
        Control control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        // Initialize Helper class for Ad Management
        adHelper = new Helper(this);


    }

    private void onDevloperProfile(View view) {

        intent = new Intent(this, DevloperProfileActivity.class);
        startActivity(intent);
        finish();

    }


    // Handle Ghost Detector button click
    private void onGhostButtonClick(View view) {

        intent = new Intent(this, GhostDetectedActivity.class);
        startActivity(intent);
        adHelper.showCounterInterstitialAd(0); // Show ad every 5 clicks
        finish();


    }


    private void onMagneticSensorButtonClick(View view) {

        intent = new Intent(this, SensorCheckActivity.class);

        startActivity(intent);
        adHelper.showInterstitialAd();

    }


    private void onDemoButtonClick(View view) {
        intent = new Intent(this, DemoActivity.class);

        startActivity(intent);
        adHelper.showInterstitialAd();
        finish();

    }

    private void onAboutButtonClick(View view) {

        intent = new Intent(this, AboutActivity.class);

        startActivity(intent);
        adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks

    }

    private void CompassButtonClick(View view) {

        intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
        adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks

        finish();

    }

    private void onMoreButton(View view) {

        intent = new Intent(this, FlashLightActivity.class);
        startActivity(intent);
        finish();

    }


}