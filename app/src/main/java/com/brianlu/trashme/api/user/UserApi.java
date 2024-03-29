package com.brianlu.trashme.api.user;

import com.brianlu.trashme.dto.UserProfileEditRequest;

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

  @POST("/TrashMe/User/Update/Location")
  @Headers({"Content-Type:application/json"})
  Observable<Response<ResponseBody>> updateLocation(
      @Header("Authorization") String authKey, @Body String body);

  @GET("/TrashMe/User/Forgot/Password")
  Observable<Response<ResponseBody>> forgotPassword(@Query("email") String email);

  @POST("/TrashMe/User/Profile/Edit")
  Observable<Response<ResponseBody>> editProfile(
      @Header("Authorization") String authKey, @Body UserProfileEditRequest body);

  @GET("/TrashMe/User/Profile")
  Observable<Response<String>> getProfile(@Header("Authorization") String authKey);
}
