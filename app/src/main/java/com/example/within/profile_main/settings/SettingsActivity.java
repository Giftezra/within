package com.example.within.profile_main.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.R;
import com.example.within.user.DataBaseHelper;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    // Change password view, about view change number view
    TextView change_number_button, about_button, change_password_button;
    DataBaseHelper db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        db = new DataBaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Find and initialize the views
        change_number_button = findViewById(R.id.change_number_btn);
        about_button = findViewById(R.id.about_btn);
        change_password_button = findViewById(R.id.change_password_btn);
        /* Setting an on listener to the change number button.to open the change number dialog*/
        change_number_button.setOnClickListener(v -> {
            showChangeNumberDialog();
        });

    }

    /** Method shows a dialog when the clicks the change number text.
     * dialog would handle the ability for the user to to change their registered phone numbe.
     * mthid uses the database helper updateUserPhone */
    private void showChangeNumberDialog (){
        /* First create a new dialog and set its content to the change number layout*/
        Dialog changeNumberDialog = new Dialog(this);
        changeNumberDialog.setContentView(R.layout.change_phone);
        /* Get the views from the layout*/
        EditText old_phone_number = changeNumberDialog.findViewById(R.id.old_phone_field);
        EditText new_phone_number = changeNumberDialog.findViewById(R.id.new_phone_field);
        EditText password_field = changeNumberDialog.findViewById(R.id.password_field);
        Button submit = changeNumberDialog.findViewById(R.id.submit_button);
        /* Check if the details entered are valid.
        * First get the value fields
        * Check if the old phone number is in system, then validate the password using the old phone number as argument
        * if the password is valid, update the db with the new phone
        * */
        String old_phone = old_phone_number.getText().toString(); // Get the old phone value of the field
        String new_phone = new_phone_number.getText().toString(); // Get the new phone value
        String password = password_field.getText().toString(); // Get the password field]
        // Set a listener to the submit button
        submit.setOnClickListener(v -> {
            if (validateOldPhone(old_phone) == true){ // Validate the old phone
                boolean password_valid = validatePassword(old_phone, password); // Validate the password
                if (password_valid){
                    boolean updated = db.updatePhoneNumber(new_phone); // Update the db if password and old number are valid
                    if (updated){
                        Toast.makeText(this, "Phone number updated successfully", Toast.LENGTH_SHORT).show(); // Show the user an update message
                    }else {
                        Toast.makeText(this, "Error updating the user password..Please contact support", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Invalid password... please use another password", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Invalid phone number.. Try again ir contact support", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the dialog title and its layout to match the parent container
        changeNumberDialog.setTitle("Contact Details");
        changeNumberDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK) {
        });

        Objects.requireNonNull(changeNumberDialog.getWindow()).setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        // Show the dialog
        changeNumberDialog.show();
    }

    /** Method validates the users phone number and returns a boolean
     * @param oldPhone
     * */
    private boolean validateOldPhone (String oldPhone){
        /* Call the getPhone method of the database helper class to check if the phone number the user entered is on the database*/
        if (db.getUserPhoneNumber(oldPhone).contains(oldPhone)){
            return true;
        }else {return false;}
    }

    /* Method validated the user password to make sure it is the same as the user who is changing the phone number*/
    public boolean validatePassword (String phone, String password){
        String salt = db.getPasswordSalt(phone);
        String hash = DataBaseHelper.hashPassword(password, salt);
        if (db.getPassword(phone) == hash){
            return true;
        }else{
            return false;
        }
    }
}
