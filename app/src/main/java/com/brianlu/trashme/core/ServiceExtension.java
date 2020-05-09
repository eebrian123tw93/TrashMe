package com.brianlu.trashme.core;

import android.util.Log;

import com.brianlu.trashme.model.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ServiceExtension {

    default Observable<Result> mapToResult(Observable<Response<ResponseBody>> responseObservable, boolean isObserveOnIO) {

        return responseObservable.subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .map(response -> {
                    Log.i("ServiceExtension", responseObservable.toString());
                    if (response.code() == 401) {
                        throw new Exception("授權失敗");
                    }
                    return response.isSuccessful()? response.body():response.errorBody();
                })
                .map(ResponseBody::string)
                .doOnNext(System.out::println)
                .map(s -> new GsonBuilder().create().fromJson(s, Result.class))
                .doOnNext(System.out::println)
                .doOnNext(Result::checkAPIResultOk);
    }

    default Observable<Map<String, Object>> mapToPayLoad(Observable<Response<ResponseBody>> response, boolean isObserveOnIO) {
        return mapToResult(response, isObserveOnIO)
                .doOnNext(Result::checkPayLoadIsNotNuLL)
                .map(Result::getPayload)
                .doOnNext(System.out::println);

    }

    default <T> Observable<T> mapPayLoadToModel(Observable<Response<ResponseBody>> response, boolean isObserveOnIO, Class<T> cls) {
        return mapToPayLoad(response, isObserveOnIO)
                .map(payload -> {
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(payload);
                    return gson.fromJson(jsonElement, cls);
                });
    }

}
