package com.example.within.contacts.recent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.within.Call;
import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.contacts.ContactActivity;
import com.example.within.contacts.ContactAdapter;
import com.example.within.contacts.DialpadActivity;
import com.example.within.profile_main.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView recent_list;
    List<RecentModel> recentModels;
    private static final int READ_CALL_LOG_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        // Set the call log recent list view to its id on the xml
        recent_list = findViewById(R.id.recent_lists_view);
        recent_list.setVerticalFadingEdgeEnabled(true);

        /* Set an onClick listener to the bottom nav bar views and display their activities */
        findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(HomeActivity.this, HomeActivity.class);
            }
        });
        // Set the on click listener to the dial pad
        findViewById(R.id.dialpad_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(HomeActivity.this, DialpadActivity.class);
            }
        });

        findViewById(R.id.contact_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(HomeActivity.this, ContactActivity.class );
            }
        });

        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToPage(HomeActivity.this, ProfileActivity.class);
            }
        });
        /* Check if the call log permissions has been granted and initialize the recent call log */
//        if (checkPermission()){
//            initializeRecent();
//        }else {
//            requestPermission();
//        }
    }
//
//    private boolean checkPermission() {
//        // Check if the permission is granted
//        return ContextCompat.checkSelfPermission(
//                this,
//                "android.permission.READ_CALL_LOG"
//        ) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private List<RecentModel> getCallLog() {
//        List<RecentModel> callLogList = new ArrayList<>(); // Create and initialize the list view
//        RecentModel model; // Make a reference of the recent model
//        // Query the call log
//        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
//                null,
//                null,
//                null,
//                CallLog.Calls.DATE + " DESC");
//
//        if (cursor != null && cursor.moveToFirst()) {
//            while (cursor.moveToFirst()) {
//                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
//                @SuppressLint("Range") long durationInSeconds = Long.parseLong(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
//                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
//                // Convert duration from seconds to minutes and seconds
//                long minutes = durationInSeconds / 60;
//                long seconds = durationInSeconds % 60;
//                /* Check if the contact has a name then display the name else display the number*/
//                model = new RecentModel(number, durationInSeconds, name);
////               /* Call the recent call log model object*/
//                callLogList.add(model);
//            }
//
//            cursor.close();
//        }
//
//        return callLogList;
//    }
//
//    /* Method initializes the list view, initializes the recent model to the getCallLog method.
//     * the recent adapter model is called*/
//    private void initializeRecent (){
//        /* Set the views and initialize the recent model list */
//        recent_list = findViewById(R.id.recent_lists_view);
//        recentModels = getCallLog();
//        RecentAdapter recentAdapter = new RecentAdapter(this, recentModels);
//        recent_list.setAdapter(recentAdapter); // Pass the adapter
//
//    }
//
//    /* Request the user call log permission */
//    private void requestPermission() {
//        // Request the permission
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.READ_CALL_LOG},
//                READ_CALL_LOG_PERMISSION_REQUEST
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == READ_CALL_LOG_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed to read call log
//                initializeRecent();
//            } else {
//                Toast.makeText(this, "Error reading call log", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}
