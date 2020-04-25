package com.brianlu.trashme.base;

import com.brianlu.trashme.model.Result;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BaseService {

    private Observable<Result> mapToResult(Observable<Response<ResponseBody>> response, boolean isObserveOnIO) {
        return response.subscribeOn(Schedulers.io())
                .observeOn(isObserveOnIO ? Schedulers.io() : AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(System.out::println)
                .map(Response::body)
                .doOnNext(System.out::println)
                .map(ResponseBody::string)
                .doOnNext(System.out::println)
                .map(s -> new GsonBuilder().create().fromJson(s, Result.class))
                .doOnNext(System.out::println)
                .doOnNext(Result::checkAPIResultOk);
    }


}
