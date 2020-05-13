package com.brianlu.trashme.base;

import android.content.Context;

public class BaseService {
  protected Context context;

  protected BaseService() {
    this.context = BaseApplication.getContext();
  }
}
