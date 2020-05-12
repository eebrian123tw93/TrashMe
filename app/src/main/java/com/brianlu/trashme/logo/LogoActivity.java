package com.brianlu.trashme.logo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.brianlu.trashme.R;
import com.brianlu.trashme.home.HomeActivity;
import com.brianlu.trashme.login.LoginActivity;

public class LogoActivity extends AppCompatActivity implements LogoView {

  private LogoPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_logo);
    presenter = new LogoPresenter(this);
  }

  @Override
  public void moveToLogin() {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }

  @Override
  public void moveToHome() {
    Intent intent = new Intent(this, HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }
}
