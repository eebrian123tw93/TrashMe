package com.brianlu.trashme.core.View;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

public interface ViewExtension {

  default void setRadius(View view, int backgroundColor) {
    int radius = view.getHeight() / 2;
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(radius);
    drawable.setColor(backgroundColor);
    view.setBackground(drawable);
  }

  default void setRadiusBorder(View view, int backgroundColor, int borderColor) {
    int radius = view.getHeight() / 2;
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(radius);
    drawable.setColor(backgroundColor);
    drawable.setStroke(2, borderColor);
    view.setBackground(drawable);
  }
}
