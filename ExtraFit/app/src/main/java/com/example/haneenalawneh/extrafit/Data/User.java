package com.example.haneenalawneh.extrafit.Data;

/**
 * Created by haneenalawneh on 12/7/17.
 */

public class User {
    private String firstName;
    private String lastName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)

    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
