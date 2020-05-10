package com.brianlu.trashme.home.orderCompleted;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

public class OrderCompletedActivity extends AppCompatActivity implements ViewExtension {

    Button replyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        replyButton = findViewById(R.id.reply_button);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setRadius(replyButton, Color.rgb(254,59,91));
        replyButton.setTextColor(Color.WHITE);
    }
}
