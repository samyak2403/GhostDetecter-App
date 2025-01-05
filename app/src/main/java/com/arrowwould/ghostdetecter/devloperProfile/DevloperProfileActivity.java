package com.arrowwould.ghostdetecter.devloperProfile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arrowwould.AdManage.Control;
import com.arrowwould.AdManage.Helper;
import com.arrowwould.ghostdetecter.GhostDetectedActivity;
import com.arrowwould.ghostdetecter.R;
import com.arrowwould.ghostdetecter.databinding.ActivityDevloperProfileBinding;
import com.arrowwould.ghostdetecter.screen.MainActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;



public class DevloperProfileActivity extends AppCompatActivity {

    // Declare the binding variable
    private ActivityDevloperProfileBinding binding;

    private Helper adHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding
        binding = ActivityDevloperProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Devloper Info");
        }

        adHelper = new Helper(this);
        //---- banner ads start ----//
        Control control = new Control(this);
        control.loadBannerAd(R.id.bannerLayout);
        //---- banner ads end ---- //


        // Set developer information
        binding.devName.setText("Samyak Kamble"); // Replace with actual developer name
        binding.devImage.setImageResource(R.drawable.samyak); // Replace with actual image resource

        // Set click listeners for links
        binding.githubLayout.setOnClickListener(view -> openUrl("https://github.com/samyak2403/"));
        binding.instagramLayout.setOnClickListener(view -> openUrl("https://instagram.com/mr_samyakkamble"));
        binding.websiteLayout.setOnClickListener(view -> openUrl("https://yourwebsite.com"));
        binding.emailLayout.setOnClickListener(view -> sendEmail("arrowwouldpro@gmail.com"));
    }

    /**
     * Opens a URL in the browser.
     *
     * @param url The URL to open.
     */
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Sends an email using an email client.
     *
     * @param email The recipient's email address.
     */
    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacting Developer");
        startActivity(intent);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            Intent intent = new Intent(DevloperProfileActivity.this, MainActivity.class);
            startActivity(intent);
            adHelper.showInterstitialAd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
