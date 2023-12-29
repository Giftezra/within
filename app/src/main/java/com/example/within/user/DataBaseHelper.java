package com.example.within.user;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.within.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Database helper class which extends the SQliteHelper class to create and handle sqlite database connections
 * create several method to insert, update and delete data from the "user" database systems.
 * */
public class DataBaseHelper extends SQLiteOpenHelper {

    /* These fields are for the hashing algorithm*/
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 32;

    private static final String DATABASE_NAME = "within.db"; // Database name
    private static final int DATABASE_VERSION = 1;

    /*
    * The create table query statement checks if the table already exists before creating the (user) table which
    * contains the user details
    * */
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT, " +
                    "last_name TEXT," +
                    "password TEXT NOT NULL," +
                    "credit_balance NUMERIC," +
                    "location TEXT," +
                    "currency_choices TEXT," +
                    "email TEXT NOT NULL," +
                    "phone_number LONG NOT NULL, " +
                    "date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "salt TEXT NOT NULL)";


    private Context context; // Create a context reference

    /**
     * Database helper constructor which takes just one argument of context
     * @param context
     * */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    /**
     * Method searches the database for the user phone number and returns a boolean if the phone number is registered
     * on the system
     * @param phone
     * @return boolean
     * */
    private boolean checkPhoneExists(String phone) {
        Cursor cursor = null; // Create an instance of a cursor
        /* Use a try resource to wrap around the piece of code so the database can be closed immediately
         * as there are no errors to catch*/
        try (SQLiteDatabase db = this.getReadableDatabase()){
            // Retrieve the id column of the user with the phone number
            String[] projection = {"phone_number"};
            // Define the selection criteria
            String selection = "phone_number = ?";
            String[] selectionArgs = {phone}; // Enter the selection argument
            // Execute the query
            cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
            // Check if the cursor has any rows and return a corresponding boolean
            if (cursor.moveToFirst()){
                return true;
            }else {
                return false;
            }
        } finally {
            // Close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* Method to copy the database file from raw resources to internal storage*/
    private void copyDatabaseFromRaw() {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.within);
            OutputStream outputStream = Files.newOutputStream(context.getDatabasePath(DATABASE_NAME).toPath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method checks for the user password using the specified password from the user to match password in the password column
     * salts and generates a hashed password with the entered password and returns a password from the row with the correct
     * hashed password
     * @param password
     * */
    public boolean checkPassword (String password, String userSalt){
        // Create a new cursor object
        Cursor cursor = null;
        // Use a try resource to open the database
        try (SQLiteDatabase db = this.getReadableDatabase()){
            String[] projections = {"password"}; // Get the password field
            String selection = "password = ?"; // Get password row where ?
            String[] selectionArgs = {hashPassword(password, userSalt)};
            // Initialize the cursor
            cursor = db.query("user", projections, selection, selectionArgs, null, null, null);
            // Check if empty then return its value if not
            if (cursor != null){
                return cursor.moveToFirst();
            }
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return false;
    }
    /** Method gets the user phone number from the database using the specified number to check the database phone number
     * column for the number that matches it. returns a cursor object
     * @return cursor
     * @param userProvidedPhone
     * */
    @SuppressLint("Range")
    public String getUserPhoneNumber(String userProvidedPhone) {
        Cursor cursor = null; // Create a cursor object
        /* Use a try resource to wrap the database sqlite object
        * Create a string array projection for the phone number column
        * Create a string selector for the phone number */
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String[] projection = {"phone_number"};
            String selection = "phone_number = ?";
            String[] selectionArgs = {userProvidedPhone}; // Use the user provided phone number as the selection argument
            // Initialize the cursor object with the query
            cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
            // Check if the cursor has any rows
            if (cursor.moveToFirst()) {
                // Return the phone number if the cursor has any rows with that number
                return cursor.getString(cursor.getColumnIndex("phone_number"));
            }else {
                Log.d("User old phone", "phone number not found");
            }
        } finally {
            // If the cursor is not null, close it
            if (cursor != null) {cursor.close();}
        }
        return null; // Return null if no matching phone number is found
    }
    /**
     * Generates a string value (salt) using the SecureRandom method of the java util class
     * create a byte array [salt] and initializes it with a [salt length] value passing the generate salt
     * as parameter to the SecureRandom new byte (salt). Base64 encodes the result and returns a string value*/
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    /** Get the user password using the (user entered phone number) as the selectionArg[phone]
     * to query the user database and uses the return data (eg password) to initialize the cursor object
     * which holds the data for retrieval
     * @param phoneNumber
     * */
    @SuppressLint("Range")
    public String getPassword (String phoneNumber){
        Cursor cursor = null; // Initialize the cursor to avoid error
        String password = null;
        /* Use a try resource to open the db connection*/
        try (SQLiteDatabase db = this.getReadableDatabase()){
            String[] projection = {"password"}; // Target data
            String selection = "phone_number = ?";// Used to filter the database
            String [] selectionArgs = {phoneNumber}; // Input to filter out
            // Query the db using the cursor object
            cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()){ // Checks if the cursor is empty
                password = cursor.getString(cursor.getColumnIndex("password")); // Get the password
            }
        }
        return password;
    }
    /** Gets the users password salt value using the users phone number as a selectArgs[phone]
     * to query the user database for the (user salt) which is used to add extra security to the users password.
     * @param phoneNumber
     * */
    @SuppressLint("Range")
    public String getPasswordSalt (String phoneNumber){
        Cursor cursor = null; // Initialize the cursor to avoid error
        String salt = null;
        /* Use a try resource to open the db connection*/
        try (SQLiteDatabase db = this.getReadableDatabase()){
            String[] projection = {"salt"}; // Target data
            String selection = "phone_number = ?";// Used to filter the database
            String [] selectionArgs = {phoneNumber}; // Input to filter out
            // Query the db using the cursor object
            cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()){ // Checks if the cursor is empty
                salt = cursor.getString(cursor.getColumnIndex("salt")); // Get the password
            }
        }
        return salt;
    }

    /* This method hashes and salts the user password using different objects like PBE key_spec and secret key factory.
     * */
    public static String hashPassword(String password, String salt) {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);

        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);
        /* Use a try catch block to catch the exception from the secret key factory*/
        try {
            SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            spec.clearPassword();
        }
        return null;
    }

    /* Method inserts data into the table using the user object accessors to get the user data*/
    public boolean insertUserData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            /* Get the date account was created*/
            long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
            Timestamp dateCreated = new Timestamp(currentTime); // Convert the current time to a timestamp

            contentValues.put("first_name", user.getFirst_name());
            contentValues.put("last_name", user.getLast_name());
            contentValues.put("password", user.getPassword());
            contentValues.put("credit_balance", user.getCredit_balance());
            contentValues.put("email", user.getEmail());
            contentValues.put("phone_number", user.getPhone_number());
            contentValues.put("date_created", dateCreated.toString());
            contentValues.put("salt", user.getSalt());

            long result = db.insert("user", null, contentValues);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    /** Method updates the users phone number in the data base with the phone number the user enters.
     * @param phoneNumber
     * @return success
     * */
    public boolean updatePhoneNumber(String phoneNumber) {
        boolean success = false;
        // Create an instance of the cursor
        Cursor cursor = null;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            // Define the projection
            String[] projection = {"phone_number"};
            String selection = "phone_number = ?";
            String[] selectionArgs = {phoneNumber};
            // Execute the query
            cursor = db.query("user", projection, selection, selectionArgs, null, null, null);
            // Check if the cursor has any rows (i.e., phone number exists)
            if (cursor.moveToFirst()) {
                /* Update the user phone number */
                ContentValues contentValues = new ContentValues();
                contentValues.put("phone_number", phoneNumber);
                int rowsUpdated = db.update("user", contentValues, selection, selectionArgs);
                success = rowsUpdated > 0; // Update successful if at least one row was affected
            } else {
                // Phone number not found, no need to update
                success = false;
            }
        } finally {
            // Close the cursor if it's not null
            if (cursor != null) {
                cursor.close();
            }
        }
        return success;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
