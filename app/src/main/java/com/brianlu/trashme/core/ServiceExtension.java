package com.brianlu.trashme.core;

import android.util.Log;

import com.brianlu.trashme.model.Result;
import com.google.gson.GsonBuilder;

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
                    return response.isSuccessful()? response.body():response.errorBody();
                })
                .map(ResponseBody::string)
                .doOnNext(System.out::println)
                .map(s -> new GsonBuilder().create().fromJson(s, Result.class))
                .doOnNext(System.out::println)
                .doOnNext(Result::checkAPIResultOk);
    }

    default Observable<String> mapToPayLoad(Observable<Response<ResponseBody>> response, boolean isObserveOnIO) {
        return mapToResult(response, isObserveOnIO)
                .doOnNext(Result::checkPayLoadIsNotNuLL)
                .map(Result::getPayload);

    }

}
