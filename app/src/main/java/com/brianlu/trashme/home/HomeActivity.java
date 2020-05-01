package com.brianlu.trashme.home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

public class HomeActivity extends AppCompatActivity implements ViewExtension {

    ImageView userPictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userPictureImageView = findViewById(R.id.userpicture_imageView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
