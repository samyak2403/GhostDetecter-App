package com.samyak2403.ghostdetecter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.arrowwould.ghostdetecter.databinding.ActivityGhostDetectedBinding;
import com.samyak2403.AdManage.Control;
import com.samyak2403.AdManage.Helper;

import com.samyak2403.ghostdetecter.screen.MainActivity;


public class GhostDetectedActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magneticSensor;

    private ActivityGhostDetectedBinding binding;

    private float magneticFieldStrength;

    private Control control;

    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityGhostDetectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar with a back arrow
        Toolbar toolbar = binding.toolbar; // Assuming your layout includes a Toolbar with ID `toolbar`
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ghost Detector"); // Set toolbar title
        }

        // Configure SpeedView
        binding.speedView.setMaxSpeed(200);
        binding.speedView.setMinSpeed(0);
        binding.speedView.speedTo(0);

        // Initialize Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        if (magneticSensor == null) {
            Toast.makeText(this, "Magnetic sensor not available on this device.", Toast.LENGTH_LONG).show();
            binding.readingText.setText("Magnetic sensor not found.");
            binding.speedView.setVisibility(View.VISIBLE);
        }

        //---- banner ads start ----//
        control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        adHelper = new Helper(this);


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            Intent intent = new Intent(GhostDetectedActivity.this, MainActivity.class);
            startActivity(intent);
            adHelper.showInterstitialAd();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            magneticFieldStrength = (float) Math.sqrt(
                    Math.pow(event.values[0], 2) +
                            Math.pow(event.values[1], 2) +
                            Math.pow(event.values[2], 2));

            updateUI();
        }
    }

    private void updateUI() {
        // Update SpeedView
        binding.speedView.speedTo(magneticFieldStrength);

        // Update TextView
        String ghostStatus;
        if (magneticFieldStrength > 100) {
            ghostStatus = "Ghost Detected! 👻";
        } else {
            ghostStatus = "No Ghosts Nearby...";
        }
        binding.readingText.setText(String.format("Magnetic Field: %.2f µT\n%s", magneticFieldStrength, ghostStatus));

        // Update bulb colors based on intensity
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
        // Handle changes in sensor accuracy
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            String accuracyMessage;
            switch (accuracy) {
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                    accuracyMessage = "Sensor accuracy: High";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                    accuracyMessage = "Sensor accuracy: Medium";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                    accuracyMessage = "Sensor accuracy: Low";
                    break;
                case SensorManager.SENSOR_STATUS_UNRELIABLE:
                    accuracyMessage = "Sensor accuracy: Unreliable";
                    break;
                default:
                    accuracyMessage = "Sensor accuracy: Unknown";
            }

            // Optionally, display the accuracy message in the UI
            binding.readingText.setText(String.format("%s\nMagnetic Field: %.2f µT",
                    accuracyMessage, magneticFieldStrength));
        }
    }
}


//old code
//package com.arrowwould.ghostdetecter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import com.arrowwould.ghostdetecter.databinding.ActivityMainBinding;
//import com.github.anastr.speedviewlib.SpeedView;
//
//public class MainActivity extends AppCompatActivity implements SensorEventListener {
//
//    private SensorManager sensorManager;
//    private Sensor magneticSensor;
//
//    private ActivityMainBinding binding;
//
//    private float magneticFieldStrength;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize View Binding
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Configure SpeedView
//        binding.speedView.setMaxSpeed(200);
//        binding.speedView.setMinSpeed(0);
//        binding.speedView.speedTo(0);
//
//        // Initialize Sensor Manager
////        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
////        if (sensorManager != null) {
////            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
////        }
////
////        // Check if the magnetic sensor is available
////        if (magneticSensor == null) {
////            Toast.makeText(this, "Magnetic sensor is required to detect ghosts!", Toast.LENGTH_LONG).show();
////            finish(); // Close the app if the sensor is not available
////        }
//        // Initialize Sensor Manager
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        if (sensorManager != null) {
//            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        }
//
//        if (magneticSensor == null) {
//            Toast.makeText(this, "Magnetic sensor not available on this device.", Toast.LENGTH_LONG).show();
//            binding.readingText.setText("Magnetic sensor not found.");
//            binding.speedView.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (magneticSensor != null) {
//            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (magneticSensor != null) {
//            sensorManager.unregisterListener(this);
//        }
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            magneticFieldStrength = (float) Math.sqrt(
//                    Math.pow(event.values[0], 2) +
//                            Math.pow(event.values[1], 2) +
//                            Math.pow(event.values[2], 2));
//
//            updateUI();
//        }
//    }
//
//    private void updateUI() {
//        // Update SpeedView
//        binding.speedView.speedTo(magneticFieldStrength);
//
//        // Update TextView
//        String ghostStatus;
//        if (magneticFieldStrength > 100) {
//            ghostStatus = "Ghost Detected! 👻";
//        } else {
//            ghostStatus = "No Ghosts Nearby...";
//        }
//        binding.readingText.setText(String.format("Magnetic Field: %.2f µT\n%s", magneticFieldStrength, ghostStatus));
//
//        // Update bulb colors based on intensity
//        resetBulbs();
//        if (magneticFieldStrength > 150) {
//            setBulbColor(binding.bulb6, Color.RED);
//            setBulbColor(binding.bulb5, Color.RED);
//        }
//        if (magneticFieldStrength > 120) {
//            setBulbColor(binding.bulb4, Color.YELLOW);
//        }
//        if (magneticFieldStrength > 90) {
//            setBulbColor(binding.bulb3, Color.GREEN);
//        }
//        if (magneticFieldStrength > 60) {
//            setBulbColor(binding.bulb2, Color.GREEN);
//        }
//        if (magneticFieldStrength > 30) {
//            setBulbColor(binding.bulb1, Color.GREEN);
//        }
//    }
//
//    private void resetBulbs() {
//        setBulbColor(binding.bulb1, Color.GRAY);
//        setBulbColor(binding.bulb2, Color.GRAY);
//        setBulbColor(binding.bulb3, Color.GRAY);
//        setBulbColor(binding.bulb4, Color.GRAY);
//        setBulbColor(binding.bulb5, Color.GRAY);
//        setBulbColor(binding.bulb6, Color.GRAY);
//    }
//
//    private void setBulbColor(CardView bulb, int color) {
//        bulb.setCardBackgroundColor(color);
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Handle changes in sensor accuracy
//        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            String accuracyMessage;
//            switch (accuracy) {
//                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
//                    accuracyMessage = "Sensor accuracy: High";
//                    break;
//                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
//                    accuracyMessage = "Sensor accuracy: Medium";
//                    break;
//                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
//                    accuracyMessage = "Sensor accuracy: Low";
//                    break;
//                case SensorManager.SENSOR_STATUS_UNRELIABLE:
//                    accuracyMessage = "Sensor accuracy: Unreliable";
//                    break;
//                default:
//                    accuracyMessage = "Sensor accuracy: Unknown";
//            }
//
//            // Optionally, display the accuracy message in the UI
//            binding.readingText.setText(String.format("%s\nMagnetic Field: %.2f µT",
//                    accuracyMessage, magneticFieldStrength));
//        }
//    }
//}
