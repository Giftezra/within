package com.example.within.contacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.within.contacts.recent.HomeActivity;
import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.profile_main.ProfileActivity;

/** The Activity handles the users dial pad and */
public class DialpadActivity extends AppCompatActivity {
    /* Initialize the views from the dial pad*/
    Button one, two, three, four, five, six, seven, eight, nine, zero, axterixs, ash;
    ImageButton call_button, delete_number;
    TextView phone_number;

    // View from the bottom nav
    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn;

    private static final int CALL_PHONE_PERMISSION_REQUEST = 1;

    // Intialize field for the country code + sign
    private StringBuilder enteredNumbers;
    private long pressStartTime;
    private long pressDuration;
    Handler handler;
    LinearLayout dial_pad;

    /* Method runs as soon as the page loads */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialpad_page);
        handler = new Handler(); // Handles the ui on the ui thread
        contact_btn = findViewById(R.id.contact_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        home_btn = findViewById(R.id.home_button);
        profile_btn = findViewById(R.id.profile_button);
        enteredNumbers = new StringBuilder();
        phone_number = findViewById(R.id.entered_number);
        call_button = findViewById(R.id.dial_call_button);
        dial_pad = findViewById(R.id.dial_pad_layout);


        // Set up number buttons
        setupNumberButton(R.id.zero, "0");
        setupNumberButton(R.id.one, "1");
        setupNumberButton(R.id.two, "2");
        setupNumberButton(R.id.three, "3");
        setupNumberButton(R.id.four, "4");
        setupNumberButton(R.id.five, "5");
        setupNumberButton(R.id.six, "6");
        setupNumberButton(R.id.seven, "7");
        setupNumberButton(R.id.eight, "8");
        setupNumberButton(R.id.nine, "9");
        setupNumberButton(R.id.asterix, "*");
        setupNumberButton(R.id.ash, "#");

        /* Set the delete button listener to delete contacts entered by the user*/
        delete_number = findViewById(R.id.delete_numbers);
        delete_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastDigit();
            }
        });

        /* Set onclick listener to the call button so it calls the make call method which gets the phone number and
        * opens a call intent*/
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call_button != null){ // Check if null to avoid null pointer
                    makeCall();
                }
            }
        });

        /* Set the listeners to the nav bar */
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(DialpadActivity.this, ContactActivity.class);
            }
        });

        dialpad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(DialpadActivity.this, DialpadActivity.class);
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(DialpadActivity.this, HomeActivity.class);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(DialpadActivity.this, ProfileActivity.class);
            }
        });

    }

    private void appendDigit(String digit) {
        enteredNumbers.append(digit);
        updateDisplay();
    }

    private void deleteLastDigit() {
        if (enteredNumbers.length() > 0) {
            enteredNumbers.deleteCharAt(enteredNumbers.length() - 1);
            updateDisplay();
        }
    }

    private final Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            // Handle long press action here
            appendDigit("+");
        }
    };

    private void makeCall() {
        // Get the phone number entered in the field
        String phone = phone_number.getText().toString();
        /* Checks if the first character is '0' or contains '+234' which indicates a local call in Nigeria,
           or if it starts with '+' indicating an international call */
        if (phone_number != null) {
            if (phone.matches("^[0-9].*") || phone.startsWith("+")) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL); // Initialize an intent to make a call
                callIntent.setData(Uri.parse("tel:" + phone)); // Parse the user phone
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            }
        }else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{"android.Manifest.permission.CALL_PHONE"},
                    CALL_PHONE_PERMISSION_REQUEST
            );
        }
    }

    /* Method gets the dail pad*/
    @SuppressLint("ClickableViewAccessibility")
    private void setupNumberButton(int buttonId, final String number) {
        Button button = findViewById(buttonId);
        Button zero = findViewById(R.id.zero);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                appendDigit(number);
            }
        });

        zero.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // Indicates button pressed
                    case MotionEvent.ACTION_DOWN:
                        pressStartTime = System.currentTimeMillis(); // Get the time the button was pressed
                        pressDuration = System.currentTimeMillis() - pressStartTime; // Get the total time press
                        handler.postDelayed(longPressRunnable, 2000); // 2 seconds delay
                        /* Check how long the button has been held and add a + sign if it is over 2 seconds and zero if less than 2sec*/
                        if (pressDuration > 2000){
                            appendDigit("+");
                        }else {
                            appendDigit("0");
                        }

                }
                return false;
            }
        });
    }

    private void updateDisplay() {
        phone_number.setText(enteredNumbers.toString());
    }
}

