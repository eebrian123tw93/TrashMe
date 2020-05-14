package com.brianlu.trashme.home.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.asksira.bsimagepicker.BSImagePicker;
import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.ConfirmDialog;
import com.brianlu.trashme.login.LoginActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileActivity extends AppCompatActivity
    implements ProfileView,
        ViewExtension,
        View.OnClickListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.OnSingleImageSelectedListener {

  private EditText profileNameEditText;
  private Button logoutButton, saveButton;

  private ImageView profilePicImageView;

  private ProfilePresenter presenter;

  private String profilePicUrl = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    profileNameEditText = findViewById(R.id.profile_name_edit_text);
    profileNameEditText.setOnClickListener(this);
    logoutButton = findViewById(R.id.logout_button);
    logoutButton.setOnClickListener(this);
    saveButton = findViewById(R.id.save_profile_button);
    saveButton.setOnClickListener(this);
    presenter = new ProfilePresenter(this);

    profilePicImageView = findViewById(R.id.profile_pic_image_view);

    //    if ()

    Glide.with(this).load("http://goo.gl/gEgYUd").into(profilePicImageView);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.logout_button:
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.setCustomTitle("登出");
        confirmDialog.setCustomMessage("確定登出");
        confirmDialog.setConfirmOnClickListener(confirmView -> presenter.logout());
        confirmDialog.show();
        break;

      case R.id.save_button:
        presenter.saveProfile(null, profileNameEditText.getText().toString(), profilePicUrl);
        break;
    }
  }

  @Override
  public void moveToLogin() {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }

  @Override
  public void onMultiImageSelected(List<Uri> uriList, String tag) {}

  @Override
  public void onSingleImageSelected(Uri uri, String tag) {}
}
