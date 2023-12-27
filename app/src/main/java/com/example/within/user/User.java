package com.example.within.user;

/** This is the user object which represents every an individual on the app. this object will have different attributes synonymous with a specifiec user*/
public class User {
    /* The user fields include (first name, last name, email, phone)*/
    private String first_name;
    private String last_name;
    private String email;
    private long phone_number;
    private double credit_balance;
    private String password;
    private String salt;
    // Constructor with arguments to initialize the user data fields
    public User(String first_name, String last_name,
                String email, long phone_number,
                double credit_balance, String password,
                String salt){
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone_number = phone_number;
        this.credit_balance = credit_balance;
        this.password = password;
        this.salt = salt;
    }

    // Getter (Accessors) for the user fields
    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public double getCredit_balance() {
        return credit_balance;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    // Generate (Accessors) for the user fields
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredit_balance(double credit_balance) {
        this.credit_balance = credit_balance;
    }
}
