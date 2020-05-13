package com.brianlu.trashme.api.consumer;

import android.annotation.SuppressLint;
import android.util.Log;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BaseService;
import com.brianlu.trashme.core.AppEnvironmentVariables;
import com.brianlu.trashme.core.ServiceExtension;
import com.brianlu.trashme.core.URLRetrofitBuilder;
import com.brianlu.trashme.model.LocationModel;
import com.brianlu.trashme.model.MainPageModel;
import com.brianlu.trashme.model.OrderModel;
import com.brianlu.trashme.model.Result;
import com.brianlu.trashme.model.User;
import com.google.gson.Gson;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class ConsumerService extends BaseService implements ServiceExtension {
  private final ConsumerApi api;

  private ConsumerService() {
    super();
    URLRetrofitBuilder urlRetrofitBuilder = new URLRetrofitBuilder();
    String baseUrl = AppEnvironmentVariables.BASE_URL;
    Retrofit retrofit = urlRetrofitBuilder.buildRetrofit(baseUrl, true);
    api = retrofit.create(ConsumerApi.class);
  }

  // 獲取實例
  public static ConsumerService getInstance() {
    return ConsumerService.SingletonHolder.INSTANCE;
  }

  public Observable<MainPageModel> mainPage(boolean isObserveOnIO) {
    User user = UserService.getInstance().user;
    String authKey = user.authKey();
    return mapPayLoadToModel(api.mainPage(authKey), isObserveOnIO, MainPageModel.class).doOnNext(model -> {
      LocationModel location = new LocationModel();
      MainPageModel.UserInfoExtended userInfoExtended = model.getUserInfoExtended();
      location.setLocationName(userInfoExtended.getLocationName());
      location.setUserType("CONSUMER");
      location.setLatitude(userInfoExtended.getLatitude());
      location.setLongitude(userInfoExtended.getLongitude());
      Log.i("ConsumerService", model.toString());
      UserService.getInstance().saveLocation(location);
    });
  }

  public Observable<Result> createOrder(OrderModel model, boolean isObserveOnIO) {
    User user = UserService.getInstance().user;
    String authKey = user.authKey();
    String json = new Gson().toJson(model);
    return mapToResult(api.orderCreate(authKey, json), isObserveOnIO);
  }

  // 創建實例
  private static class SingletonHolder {
    @SuppressLint("StaticFieldLeak")
    private static final ConsumerService INSTANCE = new ConsumerService();
  }
}
