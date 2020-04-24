package com.brianlu.trashme.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {


    @Headers("Content-Type:application/json")
    @POST("/hidp/public/register")
    Observable<Response<ResponseBody>> register(@Body String s);

    @GET("/hidp/checkUserExist/")
    Observable<Response<ResponseBody>> login(@Header("Authorization") String authKey);

    @DELETE("/hidp/deleteUser/")
    Observable<Response<ResponseBody>> deleteUser(@Header("Authorization") String authKey);

    @GET("/hidp/public/forgotPassword/")
    Observable<Response<ResponseBody>> forgotPassword(@Query("email") String email);

}
