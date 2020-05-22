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
import com.brianlu.trashme.home.location.LocationActivity;
import com.brianlu.trashme.home.profile.ProfileActivity;
import com.brianlu.trashme.home.remarks.RemarksActivity;
import com.brianlu.trashme.login.LoginActivity;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;
import com.brianlu.trashme.model.TrashType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class HomeActivity extends AppCompatActivity
    implements ViewExtension, HomeView, View.OnClickListener {

  ImageView userPictureImageView;
  TextView recycleTrashPriceTextView,
      normalTrashPriceTextView,
      mixedTrashPriceTextView,
      locationNameTextView,
      pickupOrderTimesTextView,
      noteTextView,
      mainPageNameTextView,
      estimateArrivalTimeTextView,
      orderStatusTextView;
  CardView cardView;
  HomePresenter presenter;

  ConstraintLayout orderStatusConstraintLayout;

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
    orderStatusConstraintLayout = findViewById(R.id.order_status_view);
    orderStatusTextView = findViewById(R.id.order_status_textView);
    cardView = findViewById(R.id.note_cardView);
    noteTextView = findViewById(R.id.note_textView);
    mainPageNameTextView = findViewById(R.id.main_page_name_text_view);
    estimateArrivalTimeTextView = findViewById(R.id.estimate_arrival_time_text_view);

    cardView.setOnClickListener(this);
    userPictureImageView.setOnClickListener(this);

    ConstraintLayout recycleTrashConstraintLayout = findViewById(R.id.RecycleTrashContraintLayout);
    ConstraintLayout normalTrashConstraintLayout = findViewById(R.id.NormalTrashContraintLayout);
    ConstraintLayout mixedTrashConstraintLayout = findViewById(R.id.MixedTrashContraintLayout);
    recycleTrashConstraintLayout.setOnClickListener(this);
    normalTrashConstraintLayout.setOnClickListener(this);
    mixedTrashConstraintLayout.setOnClickListener(this);

    orderStatusConstraintLayout.setVisibility(View.GONE);
    orderStatusConstraintLayout.setOnClickListener(this);
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
        Intent intentToProfile = new Intent(this, ProfileActivity.class);
        intentToProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToProfile);
        break;
      case R.id.note_cardView:
        Intent intentToRemarks = new Intent(this, RemarksActivity.class);
        intentToRemarks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToRemarks);
        break;
      case R.id.RecycleTrashContraintLayout:
        presenter.createOrder(TrashType.RECYCLE);
        break;
      case R.id.NormalTrashContraintLayout:
        presenter.createOrder(TrashType.NORMAL);
        break;
      case R.id.MixedTrashContraintLayout:
        presenter.createOrder(TrashType.MIXED);
        break;
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
  public void onSetMainPageData(MainPageModel mainPageModel) {
    recycleTrashPriceTextView.setText(mainPageModel.getRecycleTrashPrice() + "NT/kg");
    normalTrashPriceTextView.setText(mainPageModel.getNormalTrashPrice() + "NT/kg");
    mixedTrashPriceTextView.setText(mainPageModel.getMixedTrashPrice() + "NT/kg");
    pickupOrderTimesTextView.setText(
        mainPageModel.getUserInfoExtended().getPickupOrderTimes() + "");
    mainPageNameTextView.setText(mainPageModel.getUserInfoExtended().getName());
    Glide.with(this)
        .applyDefaultRequestOptions(
            new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
        .load(mainPageModel.getUserInfoExtended().getProfilePicUrl())
        .into(userPictureImageView);
  }

  @Override
  public void onSetNote(String note) {
    noteTextView.setText(note);
  }

  @Override
  public void onSetLocation(LocationModel model) {
    locationNameTextView.setText(model.getLocationName());
  }

  @Override
  public void onSetEstimateArrivalTime(String timeString) {
    estimateArrivalTimeTextView.setText(timeString);
  }

  @Override
  public void onSetOrderStatusView(int visible) {
    orderStatusConstraintLayout.setVisibility(visible);
  }

  @Override
  public void onSetOrderStateText(String text) {
    orderStatusTextView.setText(text);
  }

  @Override
  public void onSetName(String name) {
    mainPageNameTextView.setText(name);
  }
}
