package com.example.within.profile_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.contacts.ContactActivity;
import com.example.within.contacts.DialpadActivity;
import com.example.within.contacts.recent.HomeActivity;
import com.example.within.R;
import com.example.within.profile_main.settings.SettingsActivity;

/** This object is the activity for the user profile page, it extends the AppCompatActivity so it can implement some of its methods.
 * */
public class ProfileActivity extends AppCompatActivity {
    /* Fields for the image buttons in the nav bar*/
    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn;
    // Fields for the buttons in the profile page
    Button watch_ad, setting, credit, credit_other;

    /** Method is an overriden method from the extended object AppCompatActivity, it is called when the activity is created.
     * It sets the content view to the profile page and initialises the fields from the nav bar.
     * It also sets listeners to the nav bars to redirect the user to the respective activity when clicked.
     * It also sets listeners to views in the profile page and takes the use to the respective activities.
     * @param savedInstanceState
     * @override
     * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Call the super class onCreate to complete the creation of activity like
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);// Set the content view to the profile page
        /* Initialise the views in the profile page and set */
        watch_ad = findViewById(R.id.watch_ads);
        setting = findViewById(R.id.settings);
        credit = findViewById(R.id.credit);
        credit_other = findViewById(R.id.credit_others);
        /* Initialise the fields from the nav bar */
        contact_btn = findViewById(R.id.contact_button);
        home_btn = findViewById(R.id.home_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        profile_btn = findViewById(R.id.profile_button);

        /* Set listeners to the nav bars to redirect the user to the respective activity when clicked */
        // Listens to the contact button click and takes the user to the contact activity
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, ContactActivity.class);
            }
        });
        // Listens to the dial pad button
        dialpad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, DialpadActivity.class);
            }
        });
        // Listens to the home button and tak
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, HomeActivity.class);
            }
        });
        // Listens to the profile button
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, ProfileActivity.class);
            }
        });

        /* The watch_ad button takes the user to the watch ads activity.
         * The setting button takes the user to the settings activity.
        * */
        watch_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, WatchAds.class);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ProfileActivity.this, SettingsActivity.class);
            }
        });

    }


}
