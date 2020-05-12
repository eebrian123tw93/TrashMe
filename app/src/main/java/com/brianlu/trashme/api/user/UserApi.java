package com.brianlu.trashme.api.user;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface UserApi {

  @Headers("Content-Type:application/json")
  @POST("/TrashMe/User/Register")
  Observable<Response<ResponseBody>> register(@Body String s);

  @GET("/TrashMe/User/Login")
  Observable<Response<ResponseBody>> login(@Header("Authorization") String authKey);

  @GET("/TrashMe/User/Update/Location")
  Observable<Response<ResponseBody>> updateLocation(
      @Header("Authorization") String authKey, @Body String body);

  @GET("/TrashMe/User/Forgot/Password")
  Observable<Response<ResponseBody>> forgotPassword(@Query("email") String email);
}
