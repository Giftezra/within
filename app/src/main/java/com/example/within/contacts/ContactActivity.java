package com.example.within.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.calls.DialpadActivity;
import com.example.within.recent.RecentActivity;
import com.example.within.profile_main.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    /* UI field views */
    ListView contact_list_view;
    List<ContactModel> contactModels;
    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    ImageButton contact_btn, dialpad_btn, home_btn, profile_btn;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        // Get the views from the UI using the ID
        progress_bar = findViewById(R.id.progress_bar);
        contact_list_view = findViewById(R.id.contact_list);
        contact_list_view.setVerticalFadingEdgeEnabled(true);
        contact_list_view.setVerticalScrollBarEnabled(true);


        // Initialize ImageButton instances
        contact_btn = findViewById(R.id.contact_button);
        dialpad_btn = findViewById(R.id.dialpad_button);
        home_btn = findViewById(R.id.home_button);
        profile_btn = findViewById(R.id.profile_button);

        // Check for permission and initialize contacts
        if (checkPermission()) {
            initializeContacts();
        } else {
            requestPermissions();
        }

        /* Set listeners to the nav bars to redirect the user to the respective activity when clicked */
        // Listens to the contact button click and takes the user to the contact activity
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ContactActivity.this, ContactActivity.class);
            }
        });
        // Listens to the dial pad button
        dialpad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ContactActivity.this, DialpadActivity.class);
            }
        });
        // Listens to the home button and tak
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ContactActivity.this, RecentActivity.class);
            }
        });
        // Listens to the profile button
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(ContactActivity.this, ProfileActivity.class);
            }
        });
    }

    private boolean checkPermission() {
        // Check if both READ_CONTACTS and SEND_SMS permissions are granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }



    /* Method to get contacts */
    private List<ContactModel> getContacts() {
        List<ContactModel> contact_list = new ArrayList<>(); // Initialize the contact list using an array
        /* Use a cursor to query the user contact log */
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Retrieve data from the cursor and create ContactModel objects
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // Create a new ContactModel and add it to the list
                ContactModel contact = new ContactModel(displayName, phoneNumber);
                contact_list.add(contact);
            }
            cursor.close();
        }
        return contact_list;
    }

    /** Method initailizes the contact on a different thread while displaying the progress bar */
    private void initializeContacts() {
        showProgressBar(); // Show the progress bar

        new Thread(() -> {
            contactModels = getContacts();

            // Process contacts in batches (e.g., 10 contacts at a time)
            int batchSize = 50;
            for (int i = 0; i < contactModels.size(); i += batchSize) {
                final int start = i;
                final int end = Math.min(i + batchSize, contactModels.size());

                runOnUiThread(() -> {
                    // Update UI with the current batch of contacts
                    List<ContactModel> batchContacts = contactModels.subList(start, end);
                    contactModels.addAll(batchContacts);


                    // If this is the last batch, hide the progress bar
                    if (end >= 100) {
                        progress_bar.setVisibility(View.GONE);
                    }

                    // Get the views and update the contact list
                    contact_list_view = findViewById(R.id.contact_list);
                    ContactAdapter contactAdapter = new ContactAdapter(this, contactModels);
                    contact_list_view.setAdapter(contactAdapter);
                });

                // Pause for a short time to allow UI updates
                try {
                    Thread.sleep(500); // You can adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /* Method checks for asks the user for permission upon opening the activity*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_CONTACTS_PERMISSION_REQUEST:
                // Check if READ_CONTACTS permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_CONTACTS permission granted, now check for SEND_SMS permission
                    checkPermission();
                } else {
                    // READ_CONTACTS permission denied, show a message or handle it accordingly
                    Toast.makeText(this, "READ_CONTACTS Permission denied. Cannot access contacts.", Toast.LENGTH_SHORT).show();
                    // You may want to disable functionality that requires contacts here
                }
                break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                // Check if SEND_SMS permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // SEND_SMS permission granted, initialize contacts
                    initializeContacts();
                } else {
                    // SEND_SMS permission denied, show a message or handle it accordingly
                    Toast.makeText(this, "SEND_SMS Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
                    // You may want to disable functionality that requires SMS here
                }
                break;
            // Handle other permission requests if needed
        }
    }

    /* Requests the user contact permission to read the phone contact*/
    private void requestPermissions() {
        // Request both READ_CONTACTS and SEND_SMS permissions
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS
                },
                READ_CONTACTS_PERMISSION_REQUEST
        );
    }

    private void showProgressBar (){
        progress_bar.setVisibility(View.VISIBLE);

    }

}
