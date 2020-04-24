package com.brianlu.androidtemplate.Api;

import android.util.Base64;

import com.brianlu.androidtemplate.Model.User;
import com.google.gson.Gson;
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


public class ApiService {
    private Api api;

    private ApiService() {
        URLRetrofitBuilder urlRetrofitBuilder = new URLRetrofitBuilder();
        Retrofit retrofitArticleExcerptApi = urlRetrofitBuilder.buildRetrofit("http://ec2-54-169-251-7.ap-southeast-1.compute.amazonaws.com:10000/", true);
        api = retrofitArticleExcerptApi.create(Api.class);
    }

    // 獲取實例
    public static ApiService getInstance() {
        return ApiService.SingletonHolder.INSTANCE;
    }

    public Observable<Response<ResponseBody>> register(@NonNull User user, boolean isObserveOnIO) {

        Gson gson = new Gson();

        String json = gson.toJson(user);
        return api.register(json)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Response<ResponseBody>> login(@NonNull User user, boolean isObserveOnIO) {
        String authKey = user.authKey();
        return api.login(authKey)
                .subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

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

    // 創建實例
    private static class SingletonHolder {
        private static final ApiService INSTANCE = new ApiService();
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

