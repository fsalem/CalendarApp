package com.calendar.koko.calendarapp;

import android.app.Application;

/**
 * Created by farouk on 11/7/15.
 */
public class LoginCredentialsApplication extends Application {
    private String email;
    private String password;

    public LoginCredentialsApplication(){
        email = null;
        password = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
