package com.example.within.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.contacts.ContactActivity;
import com.example.within.calls.DialpadActivity;
import com.example.within.profile_main.ProfileActivity;
import com.example.within.recent.RecentActivity;

public class HomepageActivity extends AppCompatActivity {
    /* Fields for the image buttons in the nav bar*/
    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn, recent_btn;
    // Makes an instance of the user profile image view
    ImageView user_profile;
    View sideNavigation; // Create a view instance for the side nav

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        /* Initialise the fields from the nav bar */
        contact_btn = findViewById(R.id.contact_button);
        home_btn = findViewById(R.id.home_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        profile_btn = findViewById(R.id.profile_button);
        recent_btn = findViewById(R.id.recent_button);
        // Initialize the user profile image and the side navigation
        user_profile = findViewById(R.id.user_profile_image);
        sideNavigation = findViewById(R.id.side_navigation);

        /* Set listeners to the nav bars to redirect the user to the respective activity when clicked */
        // Listens to the contact button click and takes the user to the contact activity
        contact_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(HomepageActivity.this, ContactActivity.class);
        });
        // Listens to the dial pad button
        dialpad_btn.setOnClickListener(v ->  {
           NavigationManager.navigateToPage(HomepageActivity.this, DialpadActivity.class);
        });
        // Listens to the home button and set the new activity
        home_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(HomepageActivity.this, HomepageActivity.class);
        });

        // Listens to the profile button and sets the activity
        profile_btn.setOnClickListener(v -> {
           NavigationManager.navigateToPage(HomepageActivity.this, ProfileActivity.class);
        });

        // Recent button listener
        recent_btn.setOnClickListener(v -> {
            NavigationManager.navigateToPage(HomepageActivity.this, RecentActivity.class);
        });

        /*
        * Sets a listener to the profile image view to display the side navigation view when clicked*/
        user_profile.setOnClickListener(v -> {
            // Check if visible else display the view
            if (sideNavigation.getVisibility() == View.VISIBLE){
                slideOutAnimation(sideNavigation); // Hide the visibility if visible
            }else {
                slideInAnimation(sideNavigation); // Show visibility
            }
        });
    }
    /**
    * Method slide the view into display when the profile view is clicked.the display
     * implements the TranslateAnimation class
     * @param view
    * */
    private void slideInAnimation(View view) {
        // Get the width in pixels for 300dp
        @SuppressLint("ResourceType") int targetWidth = (int) getResources().getDimension(R.dimen.side_navigation_width); // Assuming you have defined this dimension in resources

        TranslateAnimation animate = new TranslateAnimation(
                -targetWidth, 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.getLayoutParams().width = targetWidth; // Set the width dynamically
        view.requestLayout(); // Request a layout update to reflect the changes
        view.setVisibility(View.VISIBLE);
    }


    private void slideOutAnimation(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0, -view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(animate);
    }

}
