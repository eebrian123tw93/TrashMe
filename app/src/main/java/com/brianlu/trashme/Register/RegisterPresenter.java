package com.brianlu.trashme.Register;

import com.brianlu.trashme.Api.ApiService;
import com.brianlu.trashme.Base.BasePresenter;
import com.brianlu.trashme.Exception.APIException;
import com.brianlu.trashme.Model.Result;
import com.brianlu.trashme.Model.User;
import com.shashank.sony.fancytoastlib.FancyToast;
import io.reactivex.Observer;

import io.reactivex.disposables.Disposable;

class RegisterPresenter extends BasePresenter {
    private RegisterView registerView;
    private User user;


    RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        user = new User();
    }

    void clear() {
        registerView.onClearText();
    }

    void doRegister(String nickname, String password, String email) {

        if (nickname.isEmpty()) {
            registerView.onSetMessage("Username can not be empty", FancyToast.ERROR);
            registerView.onRegisterResult(false);

        } else if (password.isEmpty()) {
            registerView.onSetMessage("Password can not be empty", FancyToast.ERROR);
            registerView.onRegisterResult(false);

        } else {
            user.setNickname(nickname);
            user.setPassword(password);
            user.setEmail(email);
            ApiService.getInstance().register(user, true).subscribe(new Observer<Result>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result result) {
                    registerView.onRegisterResult(true);
                    registerView.onSetMessage("Register Success", FancyToast.SUCCESS);
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof APIException) {
                        registerView.onRegisterResult(false);
                        registerView.onSetMessage(e.getMessage(), FancyToast.SUCCESS);
                    }
                }


                @Override
                public void onComplete() {

                }
            });
        }
    }

    void setProgressBarVisibility(int visibility) {
        registerView.onSetProgressBarVisibility(visibility);
    }

}
