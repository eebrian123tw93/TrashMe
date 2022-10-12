package com.brianlu.trashme.home.orderCompleted;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brianlu.trashme.R;
import com.brianlu.trashme.api.consumer.ConsumerService;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.dto.CustomResponse;
import com.brianlu.trashme.dto.PickupOrderInfo;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OrderCompletedActivity extends AppCompatActivity
    implements ViewExtension, OrderCompletedView, View.OnClickListener {

  Button replyButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_completed);
    replyButton = findViewById(R.id.reply_button);

    TextView kgTextView = findViewById(R.id.order_complete_kg_text_view);

    ConsumerService.getInstance()
        .getLatestOrder(false)
        .subscribe(
            new Observer<CustomResponse<List<PickupOrderInfo>>>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @SuppressLint("DefaultLocale")
              @Override
              public void onNext(CustomResponse<List<PickupOrderInfo>> customResponse) {
                List<PickupOrderInfo> models = customResponse.getPayload();
                if (!models.isEmpty()) {
                  kgTextView.setText(String.format("%.1f", models.get(0).getTrashWeight()) + "公斤");
                }
              }

              @Override
              public void onError(Throwable e) {
                onSetMessage(e.getMessage(), FancyToast.ERROR);
              }

              @Override
              public void onComplete() {}
            });

    replyButton.setOnClickListener(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    setRadius(replyButton, Color.rgb(254, 59, 91));
    replyButton.setTextColor(Color.WHITE);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.reply_button) {
      Intent browserIntent =
          new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/TD2TMNYufcVcsYaaA"));
      startActivity(browserIntent);
    } else if (v.getId() == R.id.back_button){
      finish();
    }
  }
}
