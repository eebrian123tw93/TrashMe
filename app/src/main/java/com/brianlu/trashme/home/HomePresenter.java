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

import org.threeten.bp.LocalDateTime;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

class HomePresenter extends BasePresenter {
  private HomeView view;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();;

  @SuppressLint("CheckResult")
  HomePresenter(HomeView view) {
    this.view = view;
    subscribeNoteRelay();
    subscribeLocationRelay();
    subscribeMessageRelay();
    subscribeUserRelay();
    getHomePageData();
  }

  void getHomePageData() {
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

  void createOrder(TrashType type) {

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

    compositeDisposable.add(
        OrderService.getInstance()
            .typeRelay
            .subscribe(
                type1 -> {
                  if (type1 == LifecycleEvent.Type.OPENED) {
                    OrderService.getInstance().createOrder(model);
                    if (compositeDisposable != null) compositeDisposable.clear();
                  }
                }));
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
                    view.onSetOrderStatusView(View.VISIBLE);
                    view.onSetOrderStateText("已建立訂單");
                    OrderService.getInstance().orderOngoing = true;

                    // todo: remove this code
                    LocalDateTime arrivalTime = LocalDateTime.now().plusMinutes(1);
                    String timeString = arrivalTime.getHour() + ":" + arrivalTime.getMinute();
                    if (arrivalTime.getHour() < 12 && arrivalTime.getHour() >= 0)
                      timeString = "上午" + timeString;
                    else if (arrivalTime.getHour() == 12) timeString = "中午" + timeString;
                    else timeString = "下午" + timeString;
                    view.onSetEstimateArrivalTime(timeString);
                    view.playRiderAnimation();
                    return;
                  case SERVER_FINISHED_ORDER:
                    view.onSetOrderStatusView(View.GONE);
                    view.onSetOrderStateText("");
                    view.onSetEstimateArrivalTime("");
                    OrderService.getInstance().disconnect();
                    getHomePageData();
                    view.onSetMessage("訂單完成", FancyToast.SUCCESS);
                    OrderService.getInstance().orderOngoing = false;
                    view.stopRiderAnimation();
                    view.moveToOrderComplete();
                    return;
                  case OTHER:
                    OrderService.getInstance().orderOngoing = false;
                    view.stopRiderAnimation();
                    return;
                }

                if (payload == null || payload.isEmpty()) {
                  return;
                }
                WaiterInfoModel waiterInfoModel = mapToModel(payload, WaiterInfoModel.class);
                String name = waiterInfoModel.getPickupUser();
                LocationModel locationModel = UserService.getInstance().locationRelay.getValue();
                int distance =
                    (int)
                        getDistanceOfMeter(
                            locationModel.getLatitude(),
                            locationModel.getLongitude(),
                            waiterInfoModel.getLatitude(),
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
                OrderService.getInstance().orderOngoing = false;
                view.stopRiderAnimation();
              }

              @Override
              public void onComplete() {}
            });
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

  private void subscribeUserRelay() {
    UserService.getInstance()
        .userBehaviorRelay
        .subscribe(
            new Observer<User>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(User model) {
                view.onSetName(model.getName());
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

  private static double getDistanceOfMeter(double lat1, double lon1, double lat2, double lon2) {

    final int R = 6371; // Radius of the earth
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(lat2)
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000; // distance
  }
}
