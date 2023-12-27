package com.example.within.contacts;

import android.graphics.Color;

public class ContactModel {
    private String name;
    private String phoneNumber;

    public ContactModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Method to get the initial letter of the contact's name
    public String getInitialLetter() {
        if (name != null && !name.isEmpty()) {
            return String.valueOf(name.charAt(0)).toUpperCase();
        } else {
            return "#";
        }
    }

    // Method to generate a color based on the contact's name
    public int getBackgroundColor() {
        String[] colors = {"#FF5733", "#33FF57", "#5733FF", "#FF5733", "#33FF57", "#5733FF"};
        int index = Math.abs(name.hashCode()) % colors.length;
        return Color.parseColor(colors[index]);
    }
}
