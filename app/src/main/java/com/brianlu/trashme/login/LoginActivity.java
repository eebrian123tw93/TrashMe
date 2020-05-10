package com.brianlu.trashme.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.home.HomeActivity;
import com.brianlu.trashme.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements ViewExtension, View.OnClickListener, LoginView {

    private LoginPresenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findViewById(R.id.register_textView).setOnClickListener(this);


        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        presenter = new LoginPresenter(this);
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

        GradientDrawable background =  new GradientDrawable();
        int loginButtonRadius = loginButton.getHeight() / 2;
        background.setCornerRadius(loginButtonRadius);
        background.setColor(Color.rgb(254,59,91));
        loginButton.setBackground(background);
        loginButton.setTextColor(Color.WHITE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_textView:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.login_button:
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                presenter.doLogin(email, password);
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
        moveToHomeActivity();
    }

    @Override
    public void onLoginFail() {

    }

    void moveToHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
        ActivityCompat.finishAffinity(this);
        startActivity(intent);
    }
}
