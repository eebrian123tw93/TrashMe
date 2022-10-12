package com.brianlu.trashme.home.pickerlocation;

import android.util.Log;
import android.view.View;

import com.brianlu.trashme.api.order.OrderService;
import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.StompMessageModel;
import com.brianlu.trashme.model.WaiterInfoModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.threeten.bp.LocalDateTime;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class PickerLocationPresenter extends BasePresenter {
  private PickerLocationView view;
  private LocationModel model;

  PickerLocationPresenter(PickerLocationView view) {
    this.view = view;

    model = UserService.getInstance().locationRelay.getValue();
    if (model != null && model.getLatitude() != null && model.getLongitude() != null) {
      view.onSetLocation(model);
    }
    subscribeMessageRelay();
  }

  private void subscribeMessageRelay() {
    OrderService.getInstance()
        .messageRelay
        .subscribe(
            new Observer<StompMessageModel>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(StompMessageModel stompMessageModel) {
                Log.i("HomePresenter", stompMessageModel.toString());
                Map<String, Object> payload = stompMessageModel.getPayload();
                switch (stompMessageModel.getOperationType()) {
                  case SERVER_CREATED_ORDER:
                    view.onSetInfo("已建立訂單");
                    return;
                  case SERVER_FINISHED_ORDER:
                    view.onSetInfo("訂單完成");
                    return;
                  case OTHER:
                    return;
                }

                if (payload == null || payload.isEmpty()) {
                  return;
                }
                WaiterInfoModel waiterInfoModel = mapToModel(payload, WaiterInfoModel.class);
                String name = waiterInfoModel.getPickupUser();
                LocationModel myLocationModel = UserService.getInstance().locationRelay.getValue();
                LocationModel pickerLocationModel = new LocationModel();
                pickerLocationModel.setLongitude(waiterInfoModel.getLongitude());
                pickerLocationModel.setLatitude(waiterInfoModel.getLatitude());
                int distance = (int) myLocationModel.getDistanceOfMeter(pickerLocationModel);
                switch (stompMessageModel.getOperationType()) {
                  case SERVER_ACCEPTED_ORDER:
                    view.onSetInfo(name + " 距離" + distance / 1000 + "公里 已接受訂單");
                    view.onSetPickerLocation(pickerLocationModel);
                    break;
                  case LOCATION_UPDATE:
                    view.onSetInfo(name + " 距離" + distance / 1000 + "公里 正在移動...");
                    view.onSetPickerLocation(pickerLocationModel);
                    break;
                }
              }

              @Override
              public void onError(Throwable e) {
                Log.i("HomePresenter", e.getMessage());
              }

              @Override
              public void onComplete() {}
            });
  }


  private <T> T mapToModel(Map<String, Object> map, Class<T> tClass) {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(map);
    return gson.fromJson(jsonElement, tClass);
  }
}
