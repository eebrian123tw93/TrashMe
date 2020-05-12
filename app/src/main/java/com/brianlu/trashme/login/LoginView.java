package com.brianlu.trashme.login;

import com.brianlu.trashme.base.BaseView;

interface LoginView extends BaseView {
  void onLoginSuccess();

  void onLoginFail();
}
