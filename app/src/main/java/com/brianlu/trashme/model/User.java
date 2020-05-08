package com.brianlu.trashme.model;


import android.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Data;
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
        return "Basic " + Base64.encodeToString(original.getBytes(), Base64.NO_WRAP);
    }
}
