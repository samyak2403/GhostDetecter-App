package com.arrowwould.ghostdetecter.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.GhostDetectedActivity;
import com.arrowwould.ghostdetecter.R;
import com.arrowwould.ghostdetecter.databinding.ActivityMainBinding;
import com.arrowwould.ghostdetecter.screen.demo.DemoActivity;


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

        //---- banner ads start ----//
        Control control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        // Initialize Helper class for Ad Management
        adHelper = new Helper(this);


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
}