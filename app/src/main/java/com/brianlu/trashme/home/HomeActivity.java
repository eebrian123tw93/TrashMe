package com.brianlu.trashme.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.ConfirmDialog;
import com.brianlu.trashme.home.location.LocationActivity;
import com.brianlu.trashme.home.remarks.RemarksActivity;
import com.brianlu.trashme.login.LoginActivity;
import com.brianlu.trashme.model.MainPageModel;
import com.shashank.sony.fancytoastlib.FancyToast;

public class HomeActivity extends AppCompatActivity
    implements ViewExtension, HomeView, View.OnClickListener {

  ImageView userPictureImageView;
  TextView recycleTrashPriceTextView,
      normalTrashPriceTextView,
      mixedTrashPriceTextView,
      locationNameTextView,
      pickupOrderTimesTextView;
  CardView cardView;
  HomePresenter presenter;
    TextView noteTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    userPictureImageView = findViewById(R.id.userpicture_imageView);

    recycleTrashPriceTextView = findViewById(R.id.recycleTrashPrice_textView);
    normalTrashPriceTextView = findViewById(R.id.normalTrashPrice_textView);
    mixedTrashPriceTextView = findViewById(R.id.mixedTrashPrice_textView);
    locationNameTextView = findViewById(R.id.locationName_textView);
    pickupOrderTimesTextView = findViewById(R.id.pickupOrderTimes_textView);
    cardView = findViewById(R.id.note_cardView);
    noteTextView = findViewById(R.id.note_textView);

    cardView.setOnClickListener(this);
    userPictureImageView.setOnClickListener(this);

    ConstraintLayout recycleTrashConstraintLayout = findViewById(R.id.RecycleTrashContraintLayout);
    ConstraintLayout normalTrashConstraintLayout = findViewById(R.id.NormalTrashContraintLayout);
    ConstraintLayout mixedTrashConstraintLayout = findViewById(R.id.MixedTrashContraintLayout);
    recycleTrashConstraintLayout.setOnClickListener(this);
    normalTrashConstraintLayout.setOnClickListener(this);
    mixedTrashConstraintLayout.setOnClickListener(this);
    presenter = new HomePresenter(this);
  }
       
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.userpicture_imageView:
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.setCustomTitle("登出");
        confirmDialog.setCustomMessage("確定登出");
        confirmDialog.setConfirmOnClickListener(view -> presenter.logout());
        confirmDialog.show();
        break;
      case R.id.note_cardView:
        Intent intentToRemarks = new Intent(this, RemarksActivity.class);
        intentToRemarks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToRemarks);
        break;
      case R.id.RecycleTrashContraintLayout:
        onSetMessage("r", FancyToast.INFO);
      case R.id.location_cardView:
        Intent intentToLocation = new Intent(this, LocationActivity.class);
        intentToLocation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToLocation);
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

  @SuppressLint("SetTextI18n")
  @Override
  public void onSetMainPageData(MainPageModel mode) {
    recycleTrashPriceTextView.setText(mode.getRecycleTrashPrice() + "NT/kg");
    normalTrashPriceTextView.setText(mode.getNormalTrashPrice() + "NT/kg");
    mixedTrashPriceTextView.setText(mode.getMixedTrashPrice() + "NT/kg");
    locationNameTextView.setText(mode.getLocationName());
    pickupOrderTimesTextView.setText(mode.getPickupOrderTimes() + "");
  }
    @Override
    public void onSetNote(String note) {
        noteTextView.setText(note);
    }
}
