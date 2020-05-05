package com.brianlu.trashme.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.brianlu.trashme.base.BaseService;
import com.brianlu.trashme.core.AppEnvironmentVariables;
import com.brianlu.trashme.core.ServiceExtension;
import com.brianlu.trashme.core.URLRetrofitBuilder;
import com.brianlu.trashme.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserService extends BaseService implements ServiceExtension {
    private final UserApi userApi;
    private User user;
    private static String PROFILE = "profile";
    private static final String USER_PROFILE = "user_profile";

    private UserService() {
        super();
        URLRetrofitBuilder urlRetrofitBuilder = new URLRetrofitBuilder();
        String baseUrl = AppEnvironmentVariables.BASE_URL;
        Retrofit retrofit = urlRetrofitBuilder.buildRetrofit(baseUrl, true);
        userApi = retrofit.create(UserApi.class);
    }

    // 創建實例
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static final UserService INSTANCE = new UserService();
    }

    public boolean isLogin() {
        return user != null;
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
        if (user == null || user.getEmail() == null || user.getPassword() == null || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            this.user = null;
        } else {
            this.user = new User(user.getEmail(), user.getPassword(), user.getEmail());
        }
    }

    // 獲取實例
    public static UserService getInstance() {
        return UserService.SingletonHolder.INSTANCE;
    }

    public Observable<User> register(@NonNull User user, boolean isObserveOnIO) {

        Gson gson = new Gson();
        String json = gson.toJson(user);

        return mapToPayLoad(userApi.register(json), isObserveOnIO)
                .map(s -> new GsonBuilder().create().fromJson(s, User.class));

    }

    public Observable<Response<ResponseBody>> login(@NonNull User user, boolean isObserveOnIO) {
        String authKey = user.authKey();
        return userApi.login(authKey)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }

    public Observable<Response<ResponseBody>> deleteUser(@NonNull User user, boolean isObserveOnIO) {
        String authKey = user.authKey();
        return userApi.deleteUser(authKey)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }

    public Observable<Response<ResponseBody>> forgotPassword(@NonNull String email, boolean isObserveOnIO) {
        return userApi.forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }




}

