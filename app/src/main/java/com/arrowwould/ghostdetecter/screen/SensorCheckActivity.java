package com.arrowwould.ghostdetecter.screen;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.R;

public class SensorCheckActivity extends AppCompatActivity {
    private static final String TAG = "SensorCheckActivity";
    private static final long SENSOR_CHECK_DELAY = 2000; // 2 seconds

    private Control control;
    private Helper adHelper;
    private SensorManager sensorManager;

    private ProgressBar progressBar;
    private TextView redCross;
    private TextView errorMessage;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_check);

        // Initialize UI components
        initializeViews();

        // Initialize Ad components
        initializeAdComponents();

        // Perform sensor check
        performSensorCheck();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.loadingProgressBar);
        redCross = findViewById(R.id.redCross);
        errorMessage = findViewById(R.id.errorMessage);
        continueButton = findViewById(R.id.testNowButton);

        // Initially hide error views
        redCross.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
    }

    private void initializeAdComponents() {
        try {
            control = new Control(this);
            control.loadBannerAd(R.id.bannerLayout);

            adHelper = new Helper(this);
        } catch (Exception e) {
            Log.e(TAG, "Ad initialization error", e);
            Toast.makeText(this, "Ad initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSensorCheck() {
        // Show progress while checking sensor
        progressBar.setVisibility(View.VISIBLE);

        progressBar.postDelayed(() -> {
            try {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                
                // Check multiple critical sensors
                boolean hasMagnetometer = checkSensor(Sensor.TYPE_MAGNETIC_FIELD);
                boolean hasAccelerometer = checkSensor(Sensor.TYPE_ACCELEROMETER);

                updateSensorCheckUI(hasMagnetometer && hasAccelerometer);
            } catch (Exception e) {
                Log.e(TAG, "Sensor check error", e);
                showSensorError("Error checking sensors");
            }
        }, SENSOR_CHECK_DELAY);
    }

    private boolean checkSensor(int sensorType) {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        return sensor != null;
    }

    private void updateSensorCheckUI(boolean sensorsAvailable) {
        progressBar.setVisibility(View.GONE);

        if (!sensorsAvailable) {
            // Sensors not found
            redCross.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Required sensors not found.\nThis app may not function correctly.");
            continueButton.setText("Continue Anyway");
        } else {
            // Sensors found
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Required Sensors Detected!");
            continueButton.setText("Proceed");
        }

        // Setup continue button click listener
        setupContinueButton(sensorsAvailable);
    }

    private void setupContinueButton(boolean sensorsAvailable) {
        continueButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(SensorCheckActivity.this, MainActivity.class);
                startActivity(intent);

                // Show interstitial ad
                if (adHelper != null) {
                    adHelper.showInterstitialAd();
                }

                finish();
            } catch (Exception e) {
                Log.e(TAG, "Navigation error", e);
                Toast.makeText(this, "Unable to proceed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSensorError(String message) {
        progressBar.setVisibility(View.GONE);
        redCross.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
        continueButton.setText("Exit");
        continueButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        // Clean up resources
        if (control != null) {
            control = null;
        }
        if (adHelper != null) {
            adHelper = null;
        }
        super.onDestroy();
    }
}
