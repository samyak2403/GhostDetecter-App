package com.arrowwould.ghostdetecter.screen;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.R;

public class SensorCheckActivity extends AppCompatActivity {


    private Control control;
    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_check);

        ProgressBar progressBar = findViewById(R.id.loadingProgressBar);
        TextView redCross = findViewById(R.id.redCross);
        TextView errorMessage = findViewById(R.id.errorMessage);
        Button continueButton = findViewById(R.id.testNowButton);

        // Show progress while checking sensor
        progressBar.setVisibility(View.VISIBLE);

        //---- banner ads start ----//
        control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        // Initialize Helper class for Ad Management
        adHelper = new Helper(this);

        // Simulate delay for sensor check
        progressBar.postDelayed(() -> {
            // Check for magnetometer
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            progressBar.setVisibility(View.GONE);

            if (magnetometer == null) {
                // Sensor not found
                redCross.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Magnetometer Sensor not Found.\nThis app cannot function without it.");
                continueButton.setText("Continue Anyway");
            } else {
                // Sensor found
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Magnetometer Sensor Detected!");
                continueButton.setText("Proceed");
            }

            // Handle button click
            continueButton.setOnClickListener(v -> {
                // Navigate to MainActivity
                Intent intent = new Intent(SensorCheckActivity.this, MainActivity.class);
                adHelper.showInterstitialAd();

                startActivity(intent);
                finish();
            });
        }, 2000); // Simulated delay




    }
}
