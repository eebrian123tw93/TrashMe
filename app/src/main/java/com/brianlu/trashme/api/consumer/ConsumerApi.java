package com.brianlu.trashme.api.consumer;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface ConsumerApi {

  @GET("/TrashMe/Consumer/MainPage")
  Observable<Response<ResponseBody>> mainPage(@Header("Authorization") String authKey);

  @POST("/TrashMe/Consumer/Order/Create")
  Observable<Response<ResponseBody>> orderCreate(
      @Header("Authorization") String authKey, @Body String body);
}
