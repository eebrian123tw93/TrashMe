package com.brianlu.trashme.login;

import android.app.ActionBar;
import android.content.Intent;
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
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.register.RegisterActivity;

import java.util.Optional;

public class LoginActivity extends AppCompatActivity implements ViewExtension, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.email_editText);
        Log.i("LoginActivity", emailEditText.getText().toString());

        findViewById(R.id.register_textView).setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CardView emailCardView = findViewById(R.id.email_cardView);
        setRadiusBorder(emailCardView,Color.WHITE,Color.rgb(254, 59, 91));
        int emailRadius = emailCardView.getHeight() / 2;
        emailCardView.setRadius(emailRadius);
        CardView passwordCardView = findViewById(R.id.password_cardView);
        setRadiusBorder(passwordCardView,Color.WHITE,Color.rgb(254, 59, 91));
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_textView) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
