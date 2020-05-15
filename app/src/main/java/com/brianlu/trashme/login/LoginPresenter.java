package com.brianlu.trashme.login;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.core.Util;
import com.brianlu.trashme.dto.CustomResponse;
import com.brianlu.trashme.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter {
  private LoginView view;

  LoginPresenter(LoginView view) {
    this.view = view;
  }

  void doLogin(String email, String password) {
    if (email.isEmpty() || password.isEmpty()) {
      view.onSetMessage("帳號或密碼不能為空", FancyToast.INFO);
    } else {
      User user = new User("", email, password);
      UserService.getInstance()
          .login(user, false)
          .subscribe(
              new Observer<Response<String>>() {
                @Override
                public void onSubscribe(Disposable d) {}

                @Override
                public void onNext(Response<String> response) {
                  if (response.isSuccessful()) {
                    try {
                      CustomResponse<User> userCustomResponse =
                          Util.OBJECT_MAPPER.readValue(
                              response.body(), new TypeReference<CustomResponse<User>>() {});
                      if (userCustomResponse != null) {
                        user.setName(userCustomResponse.getPayload().getName());
                        user.setProfilePicUrl(userCustomResponse.getPayload().getProfilePicUrl());
                      }
                    } catch (JsonProcessingException e) {
                      e.printStackTrace();
                    }
                    UserService.getInstance().saveUser(user);
                    view.onLoginSuccess();
                    view.onSetMessage("登入成功", FancyToast.SUCCESS);

                  } else {
                    view.onLoginFail();
                    view.onSetMessage("登入失敗", FancyToast.ERROR);
                  }
                }

                @Override
                public void onError(Throwable e) {
                  view.onLoginFail();
                  view.onSetMessage("登入失敗", FancyToast.ERROR);
                  e.printStackTrace();
                }

                @Override
                public void onComplete() {}
              });
    }
  }
}
