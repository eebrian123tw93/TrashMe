package com.brianlu.trashme.home.location;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.LocationModel;

interface LocationView extends BaseView {
  void onSetLocation(LocationModel model);
  void onSaveLocationSuccess();
}
