package com.brianlu.trashme.base;

import com.shashank.sony.fancytoastlib.FancyToast;

import static com.brianlu.trashme.base.BaseApplication.getContext;

public interface BaseView {
  default void onSetMessage(String message, int type) {
    FancyToast.makeText(getContext(), message, FancyToast.LENGTH_SHORT, type, false).show();
  }
}
