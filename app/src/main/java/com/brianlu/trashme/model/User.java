package com.brianlu.trashme.model;


import android.util.Base64;
import android.util.Log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {



    private String name;
    private String password;
    private String email;

    public User(){}

    public String authKey() {
        String original = email + ":" + password;
        Log.i("authKey", original);
        String authKey = "Basic " + Base64.encodeToString(original.getBytes(), Base64.NO_WRAP);
        Log.i("authKey", authKey);
        return authKey;
    }
}
