package com.brianlu.trashme.login;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
            UserService.getInstance().login(user, false).subscribe(new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {


                }

                @Override
                public void onNext(Response<ResponseBody> response) {
                    if (response.code() != 401) {
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

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

}
