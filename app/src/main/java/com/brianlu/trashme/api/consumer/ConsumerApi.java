package com.brianlu.trashme.api.consumer;

import com.brianlu.trashme.dto.CustomResponse;
import com.brianlu.trashme.dto.PickupOrderInfo;

import java.util.List;

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

  @GET("/TrashMe/Consumer/Order/Get/All")
  Observable<CustomResponse<List<PickupOrderInfo>>> getAllOrders(
      @Header("Authorization") String authKey);

  @GET("/TrashMe/Consumer/Order/Get/Latest")
  Observable<CustomResponse<List<PickupOrderInfo>>> getLatestOrder(
      @Header("Authorization") String authKey);
}
