package com.brianlu.trashme.home;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.MainPageModel;

public interface HomeView extends BaseView {
  void moveToLogin();

  void onSetMainPageData(MainPageModel mode);
}
