package com.brianlu.trashme.login;

import com.brianlu.trashme.api.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.Result;
import com.brianlu.trashme.model.User;
import com.jakewharton.rxrelay2.Relay;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter {
    private LoginView view;

    LoginPresenter(LoginView view) {
        this.view = view;
    }

    void doLogin(String email, String password){
        if(email.isEmpty() || password.isEmpty()) {
            view.onSetMessage("帳號或密碼不能為空", FancyToast.INFO);
        } else {
            User user = new User("",email, password);
            UserService.getInstance().login(user, false).subscribe(new Observer<Result>() {
                @Override
                public void onSubscribe(Disposable d) {


                }

                @Override
                public void onNext(Result result) {
                    view.onLoginSuccess();
                    view.onSetMessage("登入成功", FancyToast.SUCCESS);
                }

                @Override
                public void onError(Throwable e) {
                    view.onLoginFail();
                    view.onSetMessage(e.getMessage(), FancyToast.ERROR);
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

}
