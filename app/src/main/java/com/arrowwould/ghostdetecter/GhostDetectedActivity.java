package com.arrowwould.ghostdetecter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.arrowwould.ghostdetecter.databinding.ActivityGhostDetectedBinding;
import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.screen.MainActivity;

public class GhostDetectedActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "GhostDetectedActivity";
    private static final float GHOST_DETECTION_THRESHOLD = 100f;
    private static final float MAX_MAGNETIC_FIELD_STRENGTH = 200f;

    private SensorManager sensorManager;
    private Sensor magneticSensor;

    private ActivityGhostDetectedBinding binding;

    private float magneticFieldStrength;

    private Control adControl;
    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Initialize View Binding
            binding = ActivityGhostDetectedBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Ad components
            initializeAdComponents();

            // Setup Toolbar
            setupToolbar();

            // Initialize Sensor
            initializeSensor();

            // Configure SpeedView
            configureSpeedView();

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
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ghost Detector");
        }
    }

    private void initializeSensor() {
        // Initialize Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        if (magneticSensor == null) {
            handleMissingMagneticSensor();
        }
    }

    private void configureSpeedView() {
        binding.speedView.setMaxSpeed(MAX_MAGNETIC_FIELD_STRENGTH);
        binding.speedView.setMinSpeed(0);
        binding.speedView.speedTo(0);
    }

    private void handleMissingMagneticSensor() {
        Toast.makeText(this, "Magnetic sensor not available on this device.", Toast.LENGTH_LONG).show();
        binding.readingText.setText("Magnetic sensor not found.");
        binding.speedView.setVisibility(View.VISIBLE);
        binding.speedView.speedTo(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateBack() {
        try {
            Intent intent = new Intent(GhostDetectedActivity.this, MainActivity.class);
            startActivity(intent);
            
            if (adHelper != null) {
                adHelper.showInterstitialAd();
            }
        } catch (Exception e) {
            Log.e(TAG, "Navigation error", e);
            Toast.makeText(this, "Unable to navigate", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (magneticSensor != null) {
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (magneticSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            calculateMagneticFieldStrength(event);
            updateUI();
        }
    }

    private void calculateMagneticFieldStrength(SensorEvent event) {
        magneticFieldStrength = (float) Math.sqrt(
                Math.pow(event.values[0], 2) +
                Math.pow(event.values[1], 2) +
                Math.pow(event.values[2], 2));
    }

    private void updateUI() {
        // Update SpeedView
        binding.speedView.speedTo(magneticFieldStrength);

        // Update TextView
        String ghostStatus = determineGhostStatus();
        binding.readingText.setText(String.format("Magnetic Field: %.2f µT\n%s", magneticFieldStrength, ghostStatus));

        // Update bulb colors based on intensity
        updateBulbColors();
    }

    private String determineGhostStatus() {
        return magneticFieldStrength > GHOST_DETECTION_THRESHOLD 
            ? "Ghost Detected! 👻" 
            : "No Ghosts Nearby...";
    }

    private void updateBulbColors() {
        resetBulbs();
        
        if (magneticFieldStrength > 150) {
            setBulbColor(binding.bulb6, Color.RED);
            setBulbColor(binding.bulb5, Color.RED);
        }
        if (magneticFieldStrength > 120) {
            setBulbColor(binding.bulb4, Color.YELLOW);
        }
        if (magneticFieldStrength > 90) {
            setBulbColor(binding.bulb3, Color.GREEN);
        }
        if (magneticFieldStrength > 60) {
            setBulbColor(binding.bulb2, Color.GREEN);
        }
        if (magneticFieldStrength > 30) {
            setBulbColor(binding.bulb1, Color.GREEN);
        }
    }

    private void resetBulbs() {
        setBulbColor(binding.bulb1, Color.GRAY);
        setBulbColor(binding.bulb2, Color.GRAY);
        setBulbColor(binding.bulb3, Color.GRAY);
        setBulbColor(binding.bulb4, Color.GRAY);
        setBulbColor(binding.bulb5, Color.GRAY);
        setBulbColor(binding.bulb6, Color.GRAY);
    }

    private void setBulbColor(CardView bulb, int color) {
        bulb.setCardBackgroundColor(color);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            handleSensorAccuracyChange(accuracy);
        }
    }

    private void handleSensorAccuracyChange(int accuracy) {
        String accuracyMessage = getSensorAccuracyMessage(accuracy);
        
        // Optionally, display the accuracy message in the UI
        binding.readingText.setText(String.format("%s\nMagnetic Field: %.2f µT",
                accuracyMessage, magneticFieldStrength));
    }

    private String getSensorAccuracyMessage(int accuracy) {
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                return "Sensor accuracy: High";
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                return "Sensor accuracy: Medium";
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                return "Sensor accuracy: Low";
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                return "Sensor accuracy: Unreliable";
            default:
                return "Sensor accuracy: Unknown";
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
