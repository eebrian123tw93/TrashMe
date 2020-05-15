package com.brianlu.trashme.home.location;

import android.util.Log;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.Result;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LocationPresenter extends BasePresenter {
  private LocationView view;
  private LocationModel model;

  LocationPresenter(LocationView view) {
    this.view = view;

    model = UserService.getInstance().locationRelay.getValue();
    if (model != null && model.getLatitude() != null && model.getLongitude() != null) {
      view.onSetLocation(model);
    }
  }

  void setLocationModel(LocationModel model) {
    this.model = model;
    view.onSetLocation(model);
  }

  void updateLocation() {

    if (model == null
        || model.getLatitude() == null
        || model.getLongitude() == null
        || model.getLocationName() == null
        || model.getLocationName().isEmpty()) {
      view.onSetMessage("請點擊定位按鈕", FancyToast.INFO);
      return;
    }
    Log.i("LocationPresenter", model.toString());
    UserService.getInstance()
        .updateLocation(model, false)
        .subscribe(
            new Observer<Result>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(Result result) {
                view.onSetMessage("儲存成功", FancyToast.SUCCESS);
                view.onSaveLocationSuccess();
              }

              @Override
              public void onError(Throwable e) {
                view.onSetMessage("儲存失敗", FancyToast.ERROR);
              }

              @Override
              public void onComplete() {}
            });
  }
}
