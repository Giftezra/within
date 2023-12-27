package com.example.within.user;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.NavigationManager;
import com.example.within.contacts.recent.HomeActivity;
import com.example.within.R;

/**
 * The activity handles the user login interface and all its methods
 * */
public class LoginActivity extends AppCompatActivity {
    DataBaseHelper db;
    /*
    * UI fields
    * */
    EditText password, phone_number;
    Button login_button, biometric_login_button;
    TextView forgot_password;
    ImageView password_eye;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        db = new DataBaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        password = findViewById(R.id.password_field);
        phone_number = findViewById(R.id.email_phone_field);
        login_button = findViewById(R.id.login_button);
        biometric_login_button = findViewById(R.id.biometric_login);
        forgot_password = findViewById(R.id.forgotten_password);
        password_eye = findViewById(R.id.password_eye);
        /*
        * Sets an onclick listener to the password so the password is visible when clicked
        * */
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password_eye.setOnClickListener(v -> {
            int input_type = password.getInputType();
            if (input_type == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            password.setSelection(password.getText().length());
        });

        /* Set the on click listener for the login button*/
        login_button.setOnClickListener(v -> userLogin());
    }
    /**
     * Create a dialog to show the user they are not on the system and the dialog would
     * have a button to redirects the user to register
     * @param context
     * @param title
     * @param message
    * */
    private void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message) // Message for the user
                .setPositiveButton("Register", new DialogInterface.OnClickListener(){
                    // Navigate to the registration button if the positive button is activated
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Redirect the user to the registration page
                        NavigationManager.navigateToPage(context, RegistrationActivity.class);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    // dismiss the dialog if the dismiss is activated
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_info)
                .show();
    }
    /**
     * Method verifies the users data against the data from the system and logs the user in if valid,
     * otherwise it displays a dialog for the user to create a new account
    * */
    private void userLogin(){
        // Get the values of the user phone and password
        String user_phone = phone_number.getText().toString();
        String user_password = password.getText().toString();
        /*
        * First checks if the phone and password is empty
        * gets the user phone from the database if it matches the phone number entered by the user
        * get the password using the phone as an argument to the method in the db.
        * check if the returned password matches the the users password
        *  */
        if (!user_phone.isEmpty() || !user_password.isEmpty()) {
            // Get the phone number and salt associated with the user from the database
            String phone = db.getUserPhoneNumber(user_phone); // Get phone and password from db
            String salt = db.getPasswordSalt(user_phone);
            if (phone != null && salt != null) {
                // Check if the entered password matches the hashed password in the database
                boolean passwordMatches = db.checkPassword(user_password, salt);
                // Check password match
                if (passwordMatches) {
                    // Successful login
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
                    NavigationManager.navigateToPage(LoginActivity.this, HomeActivity.class); // Open another activity
                } else {
                    // Password does not match
                    password.setError("Invalid password");
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            } else {
                // User not found in the system
                showAlertDialog(this, "Invalid user", "Unauthorized user");
            }
        }

    }
    /**
     * Method
    * */


}
