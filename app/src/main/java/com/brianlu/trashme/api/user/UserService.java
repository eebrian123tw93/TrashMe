package com.brianlu.trashme.api.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.brianlu.trashme.base.BaseService;
import com.brianlu.trashme.core.AppEnvironmentVariables;
import com.brianlu.trashme.core.ServiceExtension;
import com.brianlu.trashme.core.URLRetrofitBuilder;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.Result;
import com.brianlu.trashme.model.User;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserService extends BaseService implements ServiceExtension {
    private final UserApi api;
    public User user;
    private static String PROFILE = "profile";
    private static final String USER_PROFILE = "user_profile";

    private UserService() {
        super();
        URLRetrofitBuilder urlRetrofitBuilder = new URLRetrofitBuilder();
        String baseUrl = AppEnvironmentVariables.BASE_URL;
        Retrofit retrofit = urlRetrofitBuilder.buildRetrofit(baseUrl, true);
        api = retrofit.create(UserApi.class);
        readUser();
    }

    // 創建實例
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static final UserService INSTANCE = new UserService();
    }

    public boolean isLogin() {
        return user != null;
    }

    public void saveUser(User user) {


        String profileJson = new Gson().toJson(user, User.class);

        context.getSharedPreferences(PROFILE, Context.MODE_PRIVATE).edit()
                .putString(USER_PROFILE, profileJson).apply();
        readUser();
    }

    private void readUser() {
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

    public Observable<Result> register(@NonNull User user, boolean isObserveOnIO) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return mapToResult(api.register(json), isObserveOnIO);
    }

    public Observable<Response<ResponseBody>> login(@NonNull User user, boolean isObserveOnIO) {
        String authKey = user.authKey();
        return api.login(authKey).subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }

    public Observable<Result> updateLocation(@NonNull LocationModel model, boolean isObserveOnIO) {
        String authKey = user.authKey();
        String json = new Gson().toJson(model);
        return mapToResult(api.updateLocation(authKey, json), isObserveOnIO);
    }


    public Observable<Response<ResponseBody>> deleteUser(@NonNull User user, boolean isObserveOnIO) {
        String authKey = user.authKey();
        return api.deleteUser(authKey)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }

    public Observable<Response<ResponseBody>> forgotPassword(@NonNull String email, boolean isObserveOnIO) {
        return api.forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }




}
