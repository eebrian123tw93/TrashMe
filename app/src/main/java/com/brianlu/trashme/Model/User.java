package com.brianlu.trashme.Model;


import android.util.Base64;

import lombok.Data;

@Data
public class User {

    private String nickname;
    private String password;
    private String email;

    public User(String nickname, String password, String email) {
        setNickname(nickname);
        setPassword(password);
        setEmail(email);
    }

    public User() {
    }

    public String authKey() {
        String original = email + ":" + password;
        return "Basic " + Base64.encodeToString(original.getBytes(), Base64.NO_WRAP);
    }
}
