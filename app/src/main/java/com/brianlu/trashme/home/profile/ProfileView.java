package com.brianlu.trashme.home.profile;

import com.brianlu.trashme.base.BaseView;
import com.brianlu.trashme.model.User;

interface ProfileView extends BaseView {
  void moveToLogin();

  void setProfileData(User user);

  void onSetProgressBarVisibility(int visibility);
}
