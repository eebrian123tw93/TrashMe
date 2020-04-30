package com.brianlu.trashme.login;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.brianlu.trashme.R;

import java.util.Optional;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.email_editText);
        Log.i("LoginActivity", emailEditText.getText().toString());

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CardView emailCardView = findViewById(R.id.email_cardView);
        int emailRadius = emailCardView.getHeight() / 2;
        emailCardView.setRadius(emailRadius);
        CardView passwordCardView = findViewById(R.id.password_cardView);
        int passwordRadius = passwordCardView.getHeight() / 2;
        passwordCardView.setRadius(passwordRadius);

        Button loginButton = findViewById(R.id.login_button);
        GradientDrawable background =  new GradientDrawable();
        int loginButtonRadius = loginButton.getHeight() / 2;
        background.setCornerRadius(loginButtonRadius);
        background.setColor(Color.rgb(254,59,91));
        loginButton.setBackground(background);
        loginButton.setTextColor(Color.WHITE);


    }
}
