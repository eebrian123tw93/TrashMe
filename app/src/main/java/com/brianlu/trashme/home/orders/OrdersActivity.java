package com.brianlu.trashme.home.orders;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brianlu.trashme.R;
import com.brianlu.trashme.api.consumer.ConsumerService;
import com.brianlu.trashme.core.ProjectUtil;
import com.brianlu.trashme.dto.CustomResponse;
import com.brianlu.trashme.dto.PickupOrderInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OrdersActivity extends AppCompatActivity {

  TextView dataTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_orders);
    dataTextView = findViewById(R.id.data_text_view);

    ConsumerService.getInstance()
        .getAllOrders(false)
        .subscribe(
            new Observer<CustomResponse<List<PickupOrderInfo>>>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(CustomResponse<List<PickupOrderInfo>> customResponse) {
                try {
                  dataTextView.setText(
                      ProjectUtil.OBJECT_MAPPER
                          .writerWithDefaultPrettyPrinter()
                          .writeValueAsString(customResponse));
                } catch (JsonProcessingException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }

              @Override
              public void onComplete() {}
            });
  }
}
