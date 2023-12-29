package com.example.within.recent;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.contacts.ContactActivity;
import com.example.within.calls.DialpadActivity;
import com.example.within.home.HomepageActivity;
import com.example.within.profile_main.ProfileActivity;

public class RecentActivity extends AppCompatActivity {
    /* Fields for the image buttons in the nav bar*/
    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn, recent_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_page);

        /* Initialise the fields from the nav bar */
        contact_btn = findViewById(R.id.contact_button);
        home_btn = findViewById(R.id.home_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        profile_btn = findViewById(R.id.profile_button);
        recent_btn = findViewById(R.id.recent_button);

        /* Set listeners to the nav bars to redirect the user to the respective activity when clicked */
        // Listens to the contact button click and takes the user to the contact activity
        contact_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(RecentActivity.this, ContactActivity.class);
        });
        // Listens to the dial pad button
        dialpad_btn.setOnClickListener(v ->  {
            NavigationManager.navigateToPage(RecentActivity.this, DialpadActivity.class);
        });
        // Listens to the home button and set the new activity
        home_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(RecentActivity.this, HomepageActivity.class);
        });

        // Listens to the profile button and sets the activity
        profile_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(RecentActivity.this, ProfileActivity.class);
        });

        // Recent button listener
        recent_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(RecentActivity.this, RecentActivity.class);
        });
    }
}
