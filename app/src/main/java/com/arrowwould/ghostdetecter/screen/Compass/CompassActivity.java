package com.arrowwould.ghostdetecter.screen.Compass;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arrowwould.ghostdetecter.databinding.ActivityCompassBinding;
import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.R;
import com.arrowwould.ghostdetecter.screen.MainActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "CompassActivity";
    private static final int ANIMATION_DURATION = 210;

    private ActivityCompassBinding binding;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    private Sensor orientationSensor;

    private Helper adHelper;
    private Control adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Initialize View Binding
            binding = ActivityCompassBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Ad components
            initializeAdComponents();

            // Setup Toolbar
            setupToolbar();

            // Initialize Sensor
            initializeSensor();

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
            getSupportActionBar().setTitle("Compass");
        }
    }

    private void initializeSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        if (sensorManager != null) {
            orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            
            if (orientationSensor == null) {
                handleMissingSensor();
            }
        } else {
            handleMissingSensor();
        }
    }

    private void handleMissingSensor() {
        Toast.makeText(this, "Orientation sensor not available on this device.", Toast.LENGTH_LONG).show();
        binding.textDegree.setText("N/A");
        binding.compass.setRotation(0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            // Get the angle around the z-axis rotated
            float degree = Math.round(event.values[0]);

            // Update degree text
            String degreeText = degree + "°";
            binding.textDegree.setText(degreeText);

            // Create a rotation animation
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // Configure animation
            ra.setDuration(ANIMATION_DURATION);
            ra.setFillAfter(true);

            // Start the animation
            binding.compass.startAnimation(ra);
            currentDegree = -degree;

        } catch (Exception e) {
            Log.e(TAG, "Error in sensor update", e);
            Toast.makeText(this, "Compass update failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        try {
            String accuracyMessage;
            switch (accuracy) {
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                    accuracyMessage = "Compass accuracy: High";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                    accuracyMessage = "Compass accuracy: Medium";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                    accuracyMessage = "Compass accuracy: Low";
                    break;
                case SensorManager.SENSOR_STATUS_UNRELIABLE:
                    accuracyMessage = "Compass accuracy: Unreliable";
                    break;
                default:
                    accuracyMessage = "Compass accuracy: Unknown";
            }
            
            Log.d(TAG, accuracyMessage);
        } catch (Exception e) {
            Log.e(TAG, "Error in accuracy change", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orientationSensor != null) {
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
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
            // Show ad every 5 clicks
            if (adHelper != null) {
                adHelper.showCounterInterstitialAd(1);
            }

            // Navigate to the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Navigation error", e);
            Toast.makeText(this, "Unable to navigate", Toast.LENGTH_SHORT).show();
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