package com.brianlu.trashme.model;

import android.util.Base64;
import android.util.Log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private String name;
  private String email;
  private String password;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public User() {}

  public String authKey() {
    String original = email + ":" + password;
    Log.i("authKey", original);
    String authKey = "Basic " + Base64.encodeToString(original.getBytes(), Base64.NO_WRAP);
    Log.i("authKey", authKey);
    return authKey;
  }
}
