package com.example.within.contacts.recent;

public class RecentModel {
    String name, phone;
    long duration;

    public RecentModel(String phone, long duration, String contact_details) {
        this.name = contact_details;
        this.duration = duration;
        this.phone = phone;
    }


    public long getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
