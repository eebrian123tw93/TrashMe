package com.brianlu.trashme.base;

import android.content.Context;
import android.os.Handler;

public class BasePresenter {
  public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
  private static final String USER_PROFILE = "user_profile";

  protected Handler handler;

  protected Context context;

  protected BasePresenter() {
    this.context = BaseApplication.getContext();
    handler = new Handler();
  }
}
