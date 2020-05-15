package com.brianlu.trashme.home;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.brianlu.trashme.api.consumer.ConsumerService;
import com.brianlu.trashme.api.order.OrderService;
import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;
import com.brianlu.trashme.model.OrderModel;
import com.brianlu.trashme.model.StompMessageModel;
import com.brianlu.trashme.model.TrashType;
import com.brianlu.trashme.model.User;
import com.brianlu.trashme.model.WaiterInfoModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

class HomePresenter extends BasePresenter {
  private HomeView view;

  @SuppressLint("CheckResult")
  HomePresenter(HomeView view) {
    this.view = view;
    subscribeNoteRelay();
    subscribeLocationRelay();
    getHomePageData();
  }

  private void getHomePageData() {
    ConsumerService.getInstance()
        .mainPage(false)
        .subscribe(
            new Observer<MainPageModel>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(MainPageModel model) {
                Log.i("HomePresenter", model.toString());
                view.onSetMainPageData(model);
              }

              @Override
              public void onError(Throwable e) {
                view.onSetMessage(e.getMessage(), FancyToast.ERROR);
                e.printStackTrace();
              }

              @Override
              public void onComplete() {}
            });
  }

  public void createOrder(TrashType type) {
    String note = UserService.getInstance().noteRelay.getValue();

    LocationModel locationModel = UserService.getInstance().locationRelay.getValue();
    if (locationModel == null
        || locationModel.getLatitude() == null
        || locationModel.getLongitude() == null
        || locationModel.getLocationName() == null
        || locationModel.getLocationName().isEmpty()) {
      view.onSetMessage("請在下方點擊您的所在地", FancyToast.INFO);
      return;
    }

    OrderModel model = new OrderModel();
    model.setTrashType(type);
    model.setAdditionalInfo(note);
    model.setLocationName(locationModel.getLocationName());
    model.setLatitude(locationModel.getLatitude());
    model.setLongitude(locationModel.getLongitude());
    OrderService.getInstance().connect();
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
                    view.onSetOrderStatusView(View.VISIBLE);
                    view.onSetOrderStateText("已建立訂單");
                    return;
                  case SERVER_FINISHED_ORDER:
                    view.onSetOrderStatusView(View.GONE);
                    view.onSetOrderStateText("");
                    onComplete();
                    return;
                  case OTHER:
                    return;
                }

                if (payload == null || payload.isEmpty()) {
                  return;
                }
                WaiterInfoModel waiterInfoModel = mapToModel(payload, WaiterInfoModel.class);
                String name = waiterInfoModel.getPickupUser();
                int distance =
                    (int)
                        getDistanceOfMeter(
                            locationModel.getLatitude(),
                            waiterInfoModel.getLatitude(),
                            locationModel.getLongitude(),
                            waiterInfoModel.getLongitude());
                switch (stompMessageModel.getOperationType()) {
                  case SERVER_ACCEPTED_ORDER:
                    view.onSetOrderStatusView(View.VISIBLE);
                    view.onSetOrderStateText(name + " 距離" + distance / 1000 + "公尺 已接受訂單");
                    return;
                  case LOCATION_UPDATE:
                    view.onSetOrderStatusView(View.VISIBLE);
                    view.onSetOrderStateText(name + " 距離" + distance / 1000 + "公尺 正在移動...");
                    return;
                }
              }

              @Override
              public void onError(Throwable e) {
                Log.i("HomePresenter", e.getMessage());
              }

              @Override
              public void onComplete() {}
            });
    OrderService.getInstance()
        .typeRelay
        .subscribe(
            new Observer<LifecycleEvent.Type>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(LifecycleEvent.Type type) {
                Log.i("HomePresenter", LifecycleEvent.Type.OPENED.toString());
                if (type == LifecycleEvent.Type.OPENED) {
                  OrderService.getInstance().createOrder(model);
                }
                onComplete();
              }

              @Override
              public void onError(Throwable e) {}

              @Override
              public void onComplete() {}
            });
  }

  void logout() {
    UserService.getInstance().saveUser(new User());
    view.moveToLogin();
  }

  private void subscribeNoteRelay() {
    UserService.getInstance()
        .noteRelay
        .subscribe(
            new Observer<String>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(String note) {
                view.onSetNote(note);
              }

              @Override
              public void onError(Throwable e) {}

              @Override
              public void onComplete() {}
            });
  }

  private void subscribeLocationRelay() {
    UserService.getInstance()
        .locationRelay
        .subscribe(
            new Observer<LocationModel>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(LocationModel model) {
                view.onSetLocation(model);
              }

              @Override
              public void onError(Throwable e) {}

              @Override
              public void onComplete() {}
            });
  }

  private <T> T mapToModel(Map<String, Object> map, Class<T> tClass) {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(map);
    return gson.fromJson(jsonElement, tClass);
  }

  public static double getDistanceOfMeter(double lat1, double lng1, double lat2, double lng2) {
    double EARTH_RADIUS = 6378137;
    double radLat1 = rad(lat1);
    double radLat2 = rad(lat2);
    double a = radLat1 - radLat2;
    double b = rad(lng1) - rad(lng2);
    double s =
        2
            * Math.asin(
                Math.sqrt(
                    Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * EARTH_RADIUS;
    s = Math.round(s * 10000) / 10000;
    return s;
  }

  private static double rad(double d) {
    return d * Math.PI / 180.0;
  }
}
