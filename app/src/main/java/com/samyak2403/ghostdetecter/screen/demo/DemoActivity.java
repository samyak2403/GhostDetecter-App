package com.samyak2403.ghostdetecter.screen.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.arrowwould.ghostdetecter.databinding.ActivityDemoBinding;
import com.samyak2403.AdManage.Control;
import com.samyak2403.AdManage.Helper;
import com.samyak2403.ghostdetecter.R;


import java.util.Random;

public class DemoActivity extends AppCompatActivity {

    private ActivityDemoBinding binding;
    private float magneticFieldStrength;
    private final Random random = new Random();

    private Helper adHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar with a back arrow
        Toolbar toolbar = binding.toolbar; // Assuming your layout includes a Toolbar with ID `toolbar`
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Demo"); // Set the title of the toolbar
        }

        // Configure SpeedView
        binding.speedView.setMaxSpeed(200);
        binding.speedView.setMinSpeed(0);
        binding.speedView.speedTo(0);

        // Display a default message for the fake sensor
        binding.readingText.setText("Using simulated magnetic field readings.");

        // Start the fake reading updates
        simulateSensorReadings();

        //---- banner ads start ----//
        Control control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //

        // Initialize Helper class for Ad Management
        adHelper = new Helper(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            onBackPressed();
            adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void simulateSensorReadings() {
        // Simulate sensor data updates
        binding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Generate a random magnetic field strength value
                magneticFieldStrength = random.nextFloat() * 200; // Range: 0 to 200 µT

                // Update the UI with the fake readings
                updateUI();

                // Schedule the next update
                simulateSensorReadings();
            }
        }, 1000); // Update every second
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
}
