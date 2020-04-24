package com.brianlu.trashme.Base;

import android.content.Context;
import android.content.SharedPreferences;

import com.brianlu.trashme.Model.User;
import com.google.gson.Gson;

public class BasePresenter {
    public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String USER_PROFILE = "user_profile";
    protected static User user =  new User("test", "test", "");
    static UserListener userListener;
    private static String PROFILE = "profile";
    protected Context context;

    public BasePresenter() {
        this.context = BaseApplication.getContext();
    }

    void saveUser(User user) {


        String profileJson = new Gson().toJson(user, User.class);

        context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE).edit()
                .putString(USER_PROFILE, profileJson).apply();
    }

    void readUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE);
        String profileJson = sharedPreferences.getString(USER_PROFILE, "");
        User user = new Gson().fromJson(profileJson, User.class);
        if (user == null || user.getUserId() == null || user.getPassword() == null || user.getUserId().isEmpty() || user.getPassword().isEmpty()) {
            BasePresenter.user = null;
        } else {
            BasePresenter.user = new User(user.getUserId(), user.getPassword(), user.getEmail());
        }
    }

    public boolean isLogin() {
        return user != null;
    }

    public interface UserListener {
        void onLogin();

        void onLogout();

        void toLoginPage();
    }

}

