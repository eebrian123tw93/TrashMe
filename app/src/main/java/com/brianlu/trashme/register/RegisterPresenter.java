package com.brianlu.trashme.register;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.Result;
import com.brianlu.trashme.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class RegisterPresenter extends BasePresenter {
    private final RegisterView registerView;
    private final User user;


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
            user.setName(nickname);
            user.setPassword(password);
            user.setEmail(email);
            UserService.getInstance().register(user, false)
                    .subscribe(new Observer<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result result) {
                            UserService.getInstance().saveUser(user);
                            registerView.onRegisterResult(true);
                            registerView.onSetMessage("Register Success", FancyToast.SUCCESS);
                        }

                        @Override
                        public void onError(Throwable e) {
                            registerView.onRegisterResult(false);
                            registerView.onSetMessage(e.getMessage(), FancyToast.ERROR);
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
