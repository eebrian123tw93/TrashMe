package com.brianlu.trashme.home;

import android.annotation.SuppressLint;
import android.util.Log;

import com.brianlu.trashme.api.consumer.ConsumerService;
import com.brianlu.trashme.api.order.OrderService;
import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;
import com.brianlu.trashme.model.OrderModel;
import com.brianlu.trashme.model.TrashType;
import com.brianlu.trashme.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Observable;

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
    if (locationModel == null ||
        locationModel.getLatitude() == null ||
        locationModel.getLongitude() == null ||
        locationModel.getLocationName() == null ||
        locationModel.getLocationName().isEmpty()) {
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
    OrderService.getInstance().typeRelay.subscribe(new Observer<LifecycleEvent.Type>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(LifecycleEvent.Type type) {
        Log.i("HomePresenter", LifecycleEvent.Type.OPENED.toString());
        if (type == LifecycleEvent.Type.OPENED){
          OrderService.getInstance().createOrder(model);
        }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    });
  }

    void logout() {
        UserService.getInstance().saveUser(new User());
        view.moveToLogin();
    }

    private void subscribeNoteRelay(){
        UserService.getInstance().noteRelay.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String note) {
                view.onSetNote(note);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void subscribeLocationRelay(){
    UserService.getInstance().locationRelay.subscribe(new Observer<LocationModel>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(LocationModel model) {
        view.onSetLocation(model);
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    });
  }
}
