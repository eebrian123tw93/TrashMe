package com.brianlu.trashme.logo;

import android.util.Log;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;

class LogoPresenter extends BasePresenter {
    private  LogoView view;

    LogoPresenter(LogoView view){
        super();
        this.view = view;
        handler.postDelayed(this::checkIfLogin, 1000);
    }

    private void checkIfLogin(){
        boolean isLogin = UserService.getInstance().isLogin();
        Log.i("checkIfLogin", ""+isLogin);
        if (isLogin) {
            view.moveToHome();
        } else {
            view.moveToLogin();
        }
    }
}
