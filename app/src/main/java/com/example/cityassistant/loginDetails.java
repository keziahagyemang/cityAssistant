package com.example.cityassistant;

import android.app.ActionBar;

public class loginDetails {

    String username, email, password, confpword, date, userId;

    public  loginDetails(){

        // leave it empty
    }

    public loginDetails(String username, String email, String password,
                        String confpword, String date, String userId){
        this.username = username;
        this.email = email;
        this.password = password;
        this.confpword = confpword;
        this.date = date;
        this.userId = userId;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getConfpword() {
        return confpword;
    }

    public void setConfpword(String confpword) {
        this.confpword = confpword;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
