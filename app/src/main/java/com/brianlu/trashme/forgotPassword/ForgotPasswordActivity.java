package com.brianlu.trashme.forgotPassword;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.LoadingDialog;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, ForgotPasswordView, ViewExtension {

    private EditText emailEditText;
    private Button sendButton;


    private ForgotPasswordPresenter forgotPasswordPresenter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.email_editText);
        sendButton = findViewById(R.id.send_button);
        loadingDialog = new LoadingDialog(this);
        sendButton.setOnClickListener(this);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CardView emailCardView = findViewById(R.id.email_cardView);
        setRadiusBorder(emailCardView, Color.WHITE,Color.rgb(254, 59, 91));

        setRadius(sendButton, Color.rgb(254,59,91));
        sendButton.setTextColor(Color.WHITE);
    }

    @Override
    public void onForgotPassword(boolean result) {
        sendButton.setEnabled(true);
        if (result) {

        }
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
            case R.id.send_button:
                sendButton.setEnabled(false);

                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                forgotPasswordPresenter.doForgotPassword(emailEditText.getText().toString());
                break;
        }
    }

}
