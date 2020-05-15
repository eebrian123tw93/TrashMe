package com.brianlu.trashme.home;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;

public interface HomeView extends BaseView {
  void moveToLogin();

  void onSetMainPageData(MainPageModel mode);
  void onSetNote(String note);
  void onSetLocation(LocationModel model);

  void onSetOrderStatusView(int visible);
  void onSetOrderStateText(String text);
}
