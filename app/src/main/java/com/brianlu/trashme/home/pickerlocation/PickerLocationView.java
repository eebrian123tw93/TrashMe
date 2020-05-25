package com.brianlu.trashme.home.pickerlocation;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.LocationModel;

interface PickerLocationView extends BaseView {
  void onSetLocation(LocationModel model);

  void onSetPickerLocation(LocationModel model);

  void onSetInfo(String info);
}
