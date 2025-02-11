package com.arrowwould.ghostdetecter.screen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.R;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "AboutActivity";
    private static final String APP_SHARE_TEXT = "👻 Check out the Ghost Detector app: https://play.google.com/store/apps/details?id=";

    private Helper adHelper;
    private Control adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_about);

            // Initialize Ad components
            initializeAdComponents();

            // Setup Toolbar
            setupToolbar();

            // Setup Button Listeners
            setupButtonListeners();

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
            getSupportActionBar().setTitle("About");
        }
    }

    private void setupButtonListeners() {
        // Handle Share App Button
        Button shareAppButton = findViewById(R.id.share_app_button);
        shareAppButton.setOnClickListener(v -> shareApp());

        // Handle Rate App Button
        Button rateAppButton = findViewById(R.id.rate_app_button);
        rateAppButton.setOnClickListener(v -> rateApp());
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, APP_SHARE_TEXT + getPackageName());
            startActivity(Intent.createChooser(shareIntent, "Share Ghost Detector via"));
        } catch (Exception e) {
            Log.e(TAG, "Share app error", e);
            Toast.makeText(this, "Unable to share app", Toast.LENGTH_SHORT).show();
        }
    }

    private void rateApp() {
        try {
            // Try Google Play Store first
            startActivity(new Intent(Intent.ACTION_VIEW, 
                Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            // If Play Store app is not available, open in browser
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, 
                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            } catch (Exception ex) {
                Log.e(TAG, "Rate app error", ex);
                Toast.makeText(this, "Unable to open app store", Toast.LENGTH_SHORT).show();
            }
        }
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
            // Show ad on back navigation
            if (adHelper != null) {
                adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks
            }
            onBackPressed();
        } catch (Exception e) {
            Log.e(TAG, "Back navigation error", e);
            Toast.makeText(this, "Navigation error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showErrorAndExit(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        finish();
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
        
        super.onDestroy();
    }
}
