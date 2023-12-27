package com.example.within.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.within.Call;
import com.example.within.R;

import java.util.List;
import java.util.Objects;

/* This adapter class */
public class ContactAdapter extends ArrayAdapter<ContactModel> implements Call {
    TextView contact_image;
    TextView contact_name;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    public ContactAdapter(@NonNull Context context, List<ContactModel> contacts) {
        super(context, 0, contacts);
    }

    /* Method checks for the sms permisson*/
    private boolean checkPermission() {
        // Check if the permission is granted
        return ContextCompat.checkSelfPermission(
                getContext(),
                "android.permission.SEND_SMS"
        ) == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /* Check if the contact item is empty then inflate the contact item xml*/
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }
        ContactModel contacts = getItem(position); // Get the current contact
        /* Get the views from the contact item xml*/
        assert convertView != null;
        contact_image = convertView.findViewById(R.id.letter_image);
        contact_name = convertView.findViewById(R.id.contact_name);
        /* Check if the contact is null*/
        if (contacts != null){
            contact_image.setText(contacts.getInitialLetter()); // Get the initial letter of the contact name
            contact_image.setBackgroundColor(contacts.getBackgroundColor()); // Set the text color for the image text
            contact_name.setText(contacts.getName());
        }
        /* Set an on_click_listener to go to another activity where the user can place calls.
        * first checks if the selected contact is null;*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactModel contact = getItem(position);
                if (contact != null) {
                    // Check for permission before displaying the dialog or request it is not already allowed
                    if (checkPermission()){
                        showContactCallDialog(contact);
                    }else {
                        requestSmsPermission();
                    }

                }
            }
        });

        return convertView;
    }

    /* Method opens a dialog when the use clicks the contact. users can perform actions on the dialog and can chose to close it too*/
    public void showContactCallDialog (ContactModel contact){
        // Create a dialog instance and get its current context
        Dialog contactDialog = new Dialog(getContext());
        // Set the dialog to the layout
        contactDialog.setContentView(R.layout.contact_call);
        // Get the views using the view id
        TextView phone_number = contactDialog.findViewById(R.id.number);
        TextView initials = contactDialog.findViewById(R.id.letter);
        TextView invite = contactDialog.findViewById(R.id.invite_link);
        TextView send_to_user = contactDialog.findViewById(R.id.credit_user);
        ImageButton call_phone = contactDialog.findViewById(R.id.call_contact);
        TextView billing_per_min = contactDialog.findViewById(R.id.billing_text);
        /* Set the text to the views and a listener to the call button to make a phone call*/
        phone_number.setText(contact.getPhoneNumber());
        initials.setText(contact.getInitialLetter());

        /* Set several listeners to the views in the contact call layout file*/
        call_phone.setOnClickListener(v -> { // Listens for the call phone button and makes a call
            makeCall();
        });
        invite.setOnClickListener(v -> { // Listens to the invite link click
            // Get the phone number in string format
            String phone = phone_number.getText().toString();
            sendSms(phone);
        });


        // Set the dialog title and its layout to match the parent container
        contactDialog.setTitle("Contact Details");
        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK) {
        });
        Objects.requireNonNull(contactDialog.getWindow()).setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        // Show the dialog
        contactDialog.show();

    }

    @Override
    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        getContext().startActivity(intent);
    }

    /* Methods request the sms user permission */
    private void requestSmsPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(),
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
    }

    /* Method sends and sms using the phones sms */
    private void sendSms (String number){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("Invite sms:" + number));
        intent.putExtra("Invite", "Hello, this is your SMS message.");
        getContext().startActivity(intent);
    }
}
