package com.brianlu.trashme.core.View.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

public class ConfirmDialog extends Dialog implements ViewExtension {

    private TextView titleTextView;
    private TextView messageTextView;
    private ConstraintLayout background;
    private Button cancelButton;
    private Button confirmButton;


    public ConfirmDialog(Activity context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int color = getContext().getResources().getColor(R.color.pink);

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm, null);

        background = view.findViewById(R.id.contraint_layout);
        cancelButton = view.findViewById(R.id.cancel_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        titleTextView = view.findViewById(R.id.title_textView);
        messageTextView = view.findViewById(R.id.message_textView);

        cancelButton.setTextColor(color);
        cancelButton.setOnClickListener(v -> dismiss());
        titleTextView.setTextColor(color);
        confirmButton.setTextColor(Color.WHITE);
        setContentView(view);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(30);
        drawable.setColor(Color.parseColor("#f2f0f0"));
        background.setBackground(drawable);
        int color = getContext().getResources().getColor(R.color.pink);
        setRadiusBorder(cancelButton, Color.parseColor("#f2f0f0"), color);
        setRadius(confirmButton, color);
    }

    public void setCustomTitle(String title) {
        titleTextView.setText(title);
    }

    public void setCustomMessage(String message) {
        messageTextView.setText(message);
    }

    public void setConfirmOnClickListener(View.OnClickListener listener) {
        confirmButton.setOnClickListener(listener);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null) window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
