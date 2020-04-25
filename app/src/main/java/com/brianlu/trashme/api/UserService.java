package com.brianlu.trashme.api;

import android.util.Base64;

import com.brianlu.trashme.core.ServiceExtension;
import com.brianlu.trashme.core.AppEnvironmentVariables;
import com.brianlu.trashme.core.URLRetrofitBuilder;
import com.brianlu.trashme.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserService implements ServiceExtension {
    private final UserApi userApi;

    private UserService() {
        URLRetrofitBuilder urlRetrofitBuilder = new URLRetrofitBuilder();
        String baseUrl = AppEnvironmentVariables.BASE_URL;
        Retrofit retrofit = urlRetrofitBuilder.buildRetrofit(baseUrl, true);
        userApi = retrofit.create(UserApi.class);
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

    // 創建實例
    private static class SingletonHolder {
        private static final UserService INSTANCE = new UserService();
    }

    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }


}

