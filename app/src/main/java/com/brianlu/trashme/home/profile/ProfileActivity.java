package com.brianlu.trashme.home.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.ConfirmDialog;
import com.brianlu.trashme.core.View.dialog.LoadingDialog;
import com.brianlu.trashme.login.LoginActivity;
import com.brianlu.trashme.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.List;

public class ProfileActivity extends AppCompatActivity
    implements ProfileView, ViewExtension, View.OnClickListener {

  private EditText profileNameEditText;
  private Button logoutButton, saveButton;

  private ImageView profilePicImageView;

  private TextView emailTextView;

  private ProfilePresenter presenter;

  private LoadingDialog loadingDialog;

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
    loadingDialog = new LoadingDialog(this);
    emailTextView = findViewById(R.id.profile_email_text_view);

    profilePicImageView = findViewById(R.id.profile_pic_image_view);
    profilePicImageView.setOnClickListener(this);
    presenter.setProfileData();
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
      case R.id.save_profile_button:
        presenter.saveProfile(profileNameEditText.getText().toString());
        break;
      case R.id.back_button:
        finish();
        break;
      case R.id.profile_pic_image_view:
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Album")
            .setDirectoryName("Image Picker")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(1)
            .setLimitMessage("只能選一張照片")
            .setRequestCode(100)
            .start();
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
  public void setProfileData(User user) {
    profileNameEditText.setText(user.getName());
    emailTextView.setText(user.getEmail());
    Glide.with(this)
        .applyDefaultRequestOptions(
            new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
        .load(user.getProfilePicUrl())
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(profilePicImageView);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    int color = getResources().getColor(R.color.pink);
    setRadius(saveButton, color);
    setRadiusBorder(logoutButton, Color.WHITE, color);
    saveButton.setTextColor(Color.WHITE);
    logoutButton.setTextColor(color);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
      List<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
      // Do stuff with image's path or id. For example:
      if (images != null && !images.isEmpty()) {
        Image image = images.get(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          Uri uri =
              Uri.withAppendedPath(
                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(image.getId()));
          Glide.with(this).load(uri).into(profilePicImageView);
          presenter.setLocalPhotoPath(uri);
        } else {
          String path = image.getPath();
          Glide.with(this).load(path).into(profilePicImageView);
          presenter.setLocalPhotoPath(path);
        }
      }
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onSetProgressBarVisibility(int visibility) {
    if (visibility == View.GONE) {
      loadingDialog.dismissLoading();
    } else {
      loadingDialog.showLoading();
    }
  }
}
