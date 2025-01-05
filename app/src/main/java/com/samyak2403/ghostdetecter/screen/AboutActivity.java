package com.samyak2403.ghostdetecter.screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.samyak2403.AdManage.Control;
import com.samyak2403.AdManage.Helper;
import com.samyak2403.ghostdetecter.R;


public class AboutActivity extends AppCompatActivity {

    private Helper adHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Initialize Helper class for Ad Management
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
            getSupportActionBar().setTitle("About");
        }

        // Handle Share App Button
        Button shareAppButton = findViewById(R.id.share_app_button);
        shareAppButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "👻 Check out the Ghost Detector app: [Your App Link]");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // Handle Rate App Button
        Button rateAppButton = findViewById(R.id.rate_app_button);
        rateAppButton.setOnClickListener(v -> {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
            startActivity(rateIntent);
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            adHelper.showCounterInterstitialAd(1); // Show ad every 5 clicks

            onBackPressed();
            return true;
            // Show ad every 5 clicks
        }
        return super.onOptionsItemSelected(item);
    }
}
