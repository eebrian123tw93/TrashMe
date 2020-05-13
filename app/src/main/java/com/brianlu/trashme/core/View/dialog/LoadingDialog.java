package com.brianlu.trashme.core.View.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

public class LoadingDialog implements ViewExtension {
  private AlertDialog dialog;

  public LoadingDialog(Activity activity) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    LayoutInflater inflater = activity.getLayoutInflater();
    @SuppressLint("InflateParams")
    View view = inflater.inflate(R.layout.dialog_loading, null);
    View background = view.findViewById(R.id.contraint_layout);
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(30);
    drawable.setColor(Color.parseColor("#f2f0f0"));
    background.setBackground(drawable);
    builder.setView(view);
    builder.setCancelable(false);
    dialog = builder.create();
  }

  public void showLoading() {
    dialog.show();
    Window window = dialog.getWindow();
    if (window != null) window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
  }

  public void dismissLoading() {
    dialog.dismiss();
  }
}
