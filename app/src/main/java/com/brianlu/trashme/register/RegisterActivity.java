package com.brianlu.trashme.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.LoadingDialog;
import com.brianlu.trashme.home.HomeActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import static com.brianlu.trashme.base.BaseApplication.getContext;

public class RegisterActivity extends AppCompatActivity
    implements RegisterView, View.OnClickListener, ViewExtension {
  private RegisterPresenter registerPresenter;

  private EditText nicknameEditText;
  private EditText passwordEditText;
  private EditText emailEditText;

  private Button registerButton;
  private Button clearButton;

  private TextView messageTextView;

  private LoadingDialog loadingDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    setContentView(R.layout.activity_register);

    nicknameEditText = findViewById(R.id.nickname_editText);
    passwordEditText = findViewById(R.id.password_editText);
    emailEditText = findViewById(R.id.email_editText);
    loadingDialog = new LoadingDialog(this);
    registerButton = findViewById(R.id.register_button);
    clearButton = findViewById(R.id.clear_button);
    messageTextView = findViewById(R.id.message_textView);

    registerButton.setOnClickListener(this);
    clearButton.setOnClickListener(this);

    registerPresenter = new RegisterPresenter(this);
    registerPresenter.setProgressBarVisibility(View.GONE);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    int color = Color.rgb(254, 59, 91);

    CardView emailCardView = findViewById(R.id.email_cardView);
    setRadiusBorder(emailCardView, Color.WHITE, color);
    int emailRadius = emailCardView.getHeight() / 2;
    emailCardView.setRadius(emailRadius);
    CardView passwordCardView = findViewById(R.id.password_cardView);
    setRadiusBorder(passwordCardView, Color.WHITE, color);
    int passwordRadius = passwordCardView.getHeight() / 2;
    passwordCardView.setRadius(passwordRadius);

    CardView nicknameCardView = findViewById(R.id.nickname_cardView);
    setRadiusBorder(nicknameCardView, Color.WHITE, color);
    int nicknameRadius = passwordCardView.getHeight() / 2;
    nicknameCardView.setRadius(nicknameRadius);

    setRadiusBorder(registerButton, color, color);
    setRadiusBorder(clearButton, Color.WHITE, color);
    registerButton.setTextColor(Color.WHITE);
    clearButton.setTextColor(color);
  }

  @Override
  public void onClearText() {
    nicknameEditText.setText("");
    passwordEditText.setText("");
    emailEditText.setText("");
    messageTextView.setText("");
  }

  @Override
  public void onRegisterResult(boolean result) {
    registerPresenter.setProgressBarVisibility(View.GONE);
    registerButton.setEnabled(true);
    clearButton.setEnabled(true);
    if (result) {
      moveToHomeActivity();
    }
  }

  void moveToHomeActivity() {
    Intent intent = new Intent(this, HomeActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    ActivityCompat.finishAffinity(this);
    startActivity(intent);
  }

  @Override
  public void onSetProgressBarVisibility(int visibility) {
    if (visibility == View.GONE) {
      loadingDialog.dismissLoading();
    } else {
      loadingDialog.showLoading();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.register_button:
        registerPresenter.setProgressBarVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
        clearButton.setEnabled(false);
        View view = this.getCurrentFocus();
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
          imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        registerPresenter.doRegister(
            nicknameEditText.getText().toString(),
            passwordEditText.getText().toString(),
            emailEditText.getText().toString());
        break;
      case R.id.clear_button:
        registerPresenter.clear();
        break;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSetMessage(String message, int type) {
    FancyToast.makeText(getContext(), message, FancyToast.LENGTH_SHORT, type, false).show();
  }
}
