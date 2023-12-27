package com.example.within.contacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.within.R;


public class ContactCallActivity extends AppCompatActivity {
    // Fields to get the intent from the contact adapter
    String phone_number;
    String initial_letter;

    TextView phone_numb, initial_let;
    ImageButton call_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_call);

        /* Get the intent and the phone number and initial alphabet sent across from the contact adapter*/
        Bundle bundle = getIntent().getExtras();
        // Check if no data was sent
        if (bundle != null) {
            phone_number = bundle.getString("PHONE NUMBER");
            initial_letter = bundle.getString("INITIAL LETTER");
        }

        /* Set the text views for the phone number and initial letter accordingly*/
        phone_numb = findViewById(R.id.number);
        initial_let = findViewById(R.id.letter);
        phone_numb.setText(phone_number);
        initial_let.setText(initial_letter);

        /* Set an onClickListener to call the contact number when the user clicks the dial button*/
        call_btn = findViewById(R.id.call_contact);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(); // Make a call
            }
        });
    }

    /* Start a call intent to make a call to the contact. make sure the user has allowed
     * call permissions before the call can go through*/
    private void makeCall() {
        // Handle runtime permission checks as needed
        if (phone_numb != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone_number));
            startActivity(callIntent);
        }
    }
}
