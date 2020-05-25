package com.brianlu.trashme.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.forgotPassword.ForgotPasswordActivity;
import com.brianlu.trashme.home.HomeActivity;
import com.brianlu.trashme.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity
    implements ViewExtension, View.OnClickListener, LoginView {

  private LoginPresenter presenter;
  private EditText emailEditText;
  private EditText passwordEditText;
  private Button loginButton;
  private TextView forgotPasswordTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    findViewById(R.id.register_textView).setOnClickListener(this);

    emailEditText = findViewById(R.id.email_editText);
    passwordEditText = findViewById(R.id.password_editText);
    forgotPasswordTextView = findViewById(R.id.forgotPassword_textView);
    loginButton = findViewById(R.id.login_button);
    loginButton.setOnClickListener(this);
    forgotPasswordTextView.setOnClickListener(this);

    presenter = new LoginPresenter(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    CardView emailCardView = findViewById(R.id.email_cardView);
    setRadiusBorder(emailCardView, Color.WHITE, Color.rgb(254, 59, 91));
    int emailRadius = emailCardView.getHeight() / 2;
    emailCardView.setRadius(emailRadius);
    CardView passwordCardView = findViewById(R.id.password_cardView);
    setRadiusBorder(passwordCardView, Color.WHITE, Color.rgb(254, 59, 91));
    int passwordRadius = passwordCardView.getHeight() / 2;
    passwordCardView.setRadius(passwordRadius);

    GradientDrawable background = new GradientDrawable();
    int loginButtonRadius = loginButton.getHeight() / 2;
    background.setCornerRadius(loginButtonRadius);
    background.setColor(Color.rgb(254, 59, 91));
    loginButton.setBackground(background);
    loginButton.setTextColor(Color.WHITE);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.register_textView:
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        break;
      case R.id.login_button:
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        presenter.doLogin(email, password);
        break;
      case R.id.forgotPassword_textView:
        Intent intentToForgotPassword = new Intent(this, ForgotPasswordActivity.class);
        intentToForgotPassword.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToForgotPassword);
        break;
    }
  }

  @Override
  public void onLoginSuccess() {
    moveToHomeActivity();
  }

  @Override
  public void onLoginFail() {}

  void moveToHomeActivity() {
    Intent intent = new Intent(this, HomeActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    ActivityCompat.finishAffinity(this);
    startActivity(intent);
  }
}
