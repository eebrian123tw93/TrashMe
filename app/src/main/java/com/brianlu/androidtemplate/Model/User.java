package com.brianlu.androidtemplate.Model;


import android.util.Base64;

import lombok.Data;

@Data
public class User {

    private String userId;
    private String password;
    private String email;

    public User(String userId, String password, String email) {
        setUserId(userId);
        setPassword(password);
        setEmail(email);
    }

    public User() {
    }

    public String authKey() {
        String original = userId + ":" + password;
        return "Basic " + Base64.encodeToString(original.getBytes(), Base64.NO_WRAP);
    }
}
