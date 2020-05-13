package com.brianlu.trashme.forgotPassword;

import android.view.View;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

class ForgotPasswordPresenter extends BasePresenter {
  private ForgotPasswordView view;

  ForgotPasswordPresenter(ForgotPasswordView view) {
    this.view = view;
  }

  void doForgotPassword(final String email) {
    view.onSetProgressBarVisibility(View.VISIBLE);
    if (email.isEmpty()) {
      view.onSetProgressBarVisibility(View.GONE);
      view.onForgotPassword(false);
      view.onSetMessage("Email cant not be empty", FancyToast.ERROR);
    } else {
      UserService.getInstance()
          .forgotPassword(email, false)
          .subscribe(
              new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {}

                @Override
                public void onNext(ResponseBody responseBody) {
                  view.onForgotPassword(true);
                  view.onSetMessage("請至" + email + "獲取密碼", FancyToast.SUCCESS);
                }

                @Override
                public void onError(Throwable e) {
                  view.onForgotPassword(false);
                  view.onSetMessage("查無此email", FancyToast.ERROR);
                  view.onForgotPassword(false);
                  view.onSetMessage(e.getMessage(), FancyToast.ERROR);
                }

                @Override
                public void onComplete() {
                  view.onSetProgressBarVisibility(View.GONE);
                }
              });
    }
  }

  void setProgressBarVisibility(int visibility) {
    view.onSetProgressBarVisibility(visibility);
  }
}
