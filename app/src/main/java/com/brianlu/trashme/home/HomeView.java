package com.brianlu.trashme.home;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;

import org.threeten.bp.LocalDateTime;

public interface HomeView extends BaseView {
  void moveToLogin();

  void onSetMainPageData(MainPageModel mode);

  void onSetNote(String note);

  void onSetLocation(LocationModel model);

  void onSetEstimateArrivalTime(String timeString);

  void onSetOrderStatusView(int visible);

  void onSetOrderStateText(String text);

  void onSetName(String name);

  void playRiderAnimation();

  void stopRiderAnimation();

  void moveToOrderComplete();
}
