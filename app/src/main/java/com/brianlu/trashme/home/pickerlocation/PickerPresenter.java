package com.brianlu.trashme.home.pickerlocation;

import android.util.Log;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.Result;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PickerPresenter extends BasePresenter {
  private PickerLocationView view;
  private LocationModel model;

  PickerPresenter(PickerLocationView view) {
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


  }
}
