package com.brianlu.trashme.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.akiniyalocts.imgur_api.ImgurClient;
import com.jakewharton.threetenabp.AndroidThreeTen;

@SuppressLint("Registered")
public class BaseApplication extends Application {
  @SuppressLint("StaticFieldLeak")
  private static Context context;

  public static Context getContext() {
    return context;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
    ImgurClient.initialize("7611ce223ea0842");
    AndroidThreeTen.init(this);
  }
}
