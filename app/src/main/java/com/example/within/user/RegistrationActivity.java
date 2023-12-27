package com.example.within.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.contacts.recent.HomeActivity;
import com.example.within.R;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.regex.PatternSyntaxException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** The registration activity handles the registrations page and all its backend service.
 * it extends the app compact activity and implements its onCreate method.
 * all methods in this activity are arrange in alphabetical order */
public class RegistrationActivity extends AppCompatActivity {
    DataBaseHelper db;
    /* Views from the UI*/
    Spinner country_code;
    EditText first_name_field, last_name_field, email, phone, password, confirm_password;
    Button create_account;
    TextView password_error;

    /** Method is called immediately the register activity is activated
     * implements several listeners and method to handle the user registration
     * @param savedInstanceState
     * method */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        db = new DataBaseHelper(this); // Initialize the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        /*
        * Code below will set the spinner adapter to the array adapter
        * */
        country_code = findViewById(R.id.country_code_spinner); // Get the spinner view
        String [] spinner_item = getResources().getStringArray(R.array.spinner_items);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, spinner_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        country_code.setAdapter(adapter); // Set the adapter to the spinner

        /*
        * Gets the password and ensure the password and confirm password match by calling
        * the password listener and the confirm password listener
        * */
        password = findViewById(R.id.password_field);
        confirm_password = findViewById(R.id.confirm_password_field);
        if (password != null && confirm_password != null) {
            password.addTextChangedListener(passwordListener); // Add change listener to the password field
            confirm_password.addTextChangedListener(confirmPasswordListener); // Add change listener to the confirm password field
        }
        /*
        * Listens to the country code spinner object and sets its selected value to the phone number field
        * example +234 for Nigeria
        * */
        country_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                /* Get the selected item from the spinner and append it to the phone number field*/
                String selectedItem = parent.getItemAtPosition(position).toString();
                phone = findViewById(R.id.phone_number);
                phone.append(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        /* Sets the listener to the registration button*/
        create_account = findViewById(R.id.register_button);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    /**
     * Method creates a new user when called.
     * the method uses other helper methods to implement some of the functions.
     * the data entered by the user is validated and called within the method to validate the users the datails and if it meets the
     * criteria, the user object initialized and passed the user data, thereby creating a new user object.
     * the new user is inserted into the database and redirected to the home page activity
     * */
    private void createUser (){
        /*
        * User fields
        * */
        first_name_field = findViewById(R.id.first_name_field);
        last_name_field = findViewById(R.id.last_name_field);
        email = findViewById(R.id.email_field);
        phone = findViewById(R.id.phone_number);
        password = findViewById(R.id.password_field);
        /*
        * Make sure the field are not empty before getting its values
        * */
        if (first_name_field != null && last_name_field != null && email != null && phone != null && password != null) {
            /* Get the user data from the view fields*/
            String first_name = first_name_field.getText().toString();
            String last_name1 = last_name_field.getText().toString();
            String email1 = email.getText().toString();
            String phone_number = phone.getText().toString();
            String password1 = password.getText().toString();
            /*
            * Generate salt and hash the password
            * Hash the password and salt it
            * */
            String salt = DataBaseHelper.generateSalt();
            String hashedPassword = DataBaseHelper.hashPassword(password1, salt);

            /* Create a new user object and parse the required user details*/
            User user = new User(first_name, last_name1, email1,
                                Long.parseLong(phone_number), 0.0,
                                hashedPassword, salt);

            Log.d("New user", "new user created");
            // Check is hashed password is valid before inserting user data into the database
            if (hashedPassword != null) {
                boolean isInserted = db.insertUserData(user);
                if (isInserted) {
                    // If data inserted succefully, make a toast to let the user know and start a new intent to the homepage
                    Toast.makeText(RegistrationActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Account not created", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    /**
     * This method listens to the password field for text changes
     * */
    private final TextWatcher passwordListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(android.text.Editable s) {
            validatePassword();
        } // Validated the password after text change
    };
    /**
     * This method listens to the confirm password field and makes sure
     * it meets the criteria in the validate password method
     * */
    private final TextWatcher confirmPasswordListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(android.text.Editable s) {
            validatePassword();
        }
    };
    /**
     * The method validates the user password making sure the user does not use their names.
     * makes sure the password is not less than 8 digits and contains digits,and at-least one block letter.
     * makes sure the passwords match.
     * */
    private void validatePassword() {
        String password1 = password.getText().toString(); // Get password value
        String password2 = confirm_password.getText().toString(); // Get confirm password value
        password_error = findViewById(R.id.passwords_do_not_match_text); // Get the password error text view
        // Initialize the first and last names
        first_name_field = findViewById(R.id.first_name_field);
        last_name_field = findViewById(R.id.last_name_field);

        // Check if the password and confirm password match
        if (!password1.equals(password2)) {
            password.setError(getString(R.string.passwor_not_match)); // Set error if not a match
            password_error.setText(getString(R.string.passwor_not_match));
            return; // exit the method if passwords don't match
        }

        // Check if the password contains the user's name
        if (first_name_field != null && last_name_field != null) {
            String first_name = first_name_field.getText().toString();
            String last_name = last_name_field.getText().toString();
            if (password1.contains(first_name) || password1.contains(last_name)) {
                password.setError("Password cannot contain your name");
                password_error.setText(getString(R.string.password_error));
                return; // exit the method if password contains the name
            }
        }
        try {
            // Check if the password is less than 8 digits and contains an uppercase, lowercase, and numbers
            if (password1.length() < 8 || !password1.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+")) {
                password.setError(getString(R.string.password_error));
                password_error.setText(getString(R.string.password_error));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }


}
