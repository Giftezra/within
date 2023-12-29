package com.example.within.profile_main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.contacts.ContactActivity;
import com.example.within.calls.DialpadActivity;
import com.example.within.home.HomepageActivity;
import com.example.within.profile_main.settings.SettingsActivity;
import com.example.within.recent.RecentActivity;
import com.example.within.R;

/** This object is the activity for the user profile page, it extends the AppCompatActivity so it can implement some of its methods.
 * */
public class ProfileActivity extends AppCompatActivity {
    /* Fields for the image buttons in the nav bar*/
    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn, recent_btn;
    // Fields for the buttons in the profile page
    Button watch_ad_btn, setting_btn, add_credit_btn, credit_other_btn;

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
        watch_ad_btn = findViewById(R.id.watch_ads);
        setting_btn = findViewById(R.id.settings);
        add_credit_btn = findViewById(R.id.credit);
        credit_other_btn = findViewById(R.id.credit_others);

        /* Initialise the fields from the nav bar */
        contact_btn = findViewById(R.id.contact_button);
        home_btn = findViewById(R.id.home_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        profile_btn = findViewById(R.id.profile_button);
        recent_btn = findViewById(R.id.recent_button);

        /*
        * Set listeners to the nav bars to redirect the user to the respective activity when clicked
        * */
        // Listens to the contact button click and takes the user to the contact activity
        contact_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(ProfileActivity.this, ContactActivity.class);
        });
        // Listens to the dial pad button
        dialpad_btn.setOnClickListener(v ->  {
            NavigationManager.navigateToPage(ProfileActivity.this, DialpadActivity.class);
        });
        // Listens to the home button and set the new activity
        home_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(ProfileActivity.this, HomepageActivity.class);
        });
        // Listens to the profile button and sets the activity
        profile_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(ProfileActivity.this, ProfileActivity.class);
        });
        // Recent button listener
        recent_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(ProfileActivity.this, RecentActivity.class);
        });

        /*
        * Listens to the buttons in the profile page
        * */
        setting_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(ProfileActivity.this, SettingsActivity.class);
        });

    }


}
