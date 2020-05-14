package com.brianlu.trashme.api.order;

import android.annotation.SuppressLint;
import android.util.Log;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BaseService;
import com.brianlu.trashme.core.AppEnvironmentVariables;
import com.brianlu.trashme.core.ServiceExtension;
import com.brianlu.trashme.model.OperationType;
import com.brianlu.trashme.model.OrderModel;
import com.brianlu.trashme.model.StompMessageModel;
import com.brianlu.trashme.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompMessage;

public class OrderService extends BaseService implements ServiceExtension {

  private CompositeDisposable compositeDisposable;
  private StompClient stompClient;
  private String url = AppEnvironmentVariables.BASE_URL + AppEnvironmentVariables.WEB_SOCKET_URL ;
  private String topic = AppEnvironmentVariables.WEB_SOCKET_TOPIC;
  private String sendPath = AppEnvironmentVariables.WEB_SOCKET_SEND_PATH;
  private Disposable topicDisposable;

  public BehaviorRelay<LifecycleEvent.Type> typeRelay = BehaviorRelay.createDefault(LifecycleEvent.Type.CLOSED);
  public PublishRelay<StompMessageModel> messageRelay = PublishRelay.create();


  private OrderService() {
    typeRelay.subscribe(new Observer<LifecycleEvent.Type>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(LifecycleEvent.Type type) {
        if (type == LifecycleEvent.Type.CLOSED) {
          resetSubscriptions();
        }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    });

  }

  // 獲取實例
  public static OrderService getInstance() {
    return OrderService.SingletonHolder.INSTANCE;
  }

  // 創建實例
  private static class SingletonHolder {
    @SuppressLint("StaticFieldLeak")
    private static final OrderService INSTANCE = new OrderService();
  }

  public void connect() {

    if(stompClient != null && stompClient.isConnected()) {
      disconnect();
    }

    User user = UserService.getInstance().user;
    String authKey = user.authKey();

    Map<String, String> httpHeaders = new HashMap<>();
    httpHeaders.put("Authorization", authKey);

    OkHttpClient okHttpClient = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build();
    stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, httpHeaders, okHttpClient);
    stompClient.withClientHeartbeat(10000).withServerHeartbeat(10000);
    resetSubscriptions();

    Disposable disposableLifecycle = stompClient.lifecycle()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("OrderService", "Throwable " + throwable.getMessage()))
        .subscribe(lifecycleEvent -> {
          typeRelay.accept(lifecycleEvent.getType());
        }, Throwable::printStackTrace);

    compositeDisposable.add(disposableLifecycle);

    Flowable<StompMessage> stompObservable = stompClient
        .topic(topic)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    topicDisposable =
        mapToMessageModel(stompObservable).subscribe(topicMessage -> {messageRelay.accept(topicMessage);}, Throwable::printStackTrace);

    compositeDisposable.add(topicDisposable);
    stompClient.connect();
  }

  public void disconnect() {
    typeRelay.accept(LifecycleEvent.Type.CLOSED);
    stompClient.disconnect();
    resetSubscriptions();
  }

  private void resetSubscriptions() {
    if (compositeDisposable != null) {
      compositeDisposable.dispose();
    }
    compositeDisposable = new CompositeDisposable();
  }

  private CompletableTransformer applySchedulers() {
    return upstream -> upstream
        .unsubscribeOn(Schedulers.newThread())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private void sendMessage(String destination, String data) {
    Log.i("OrderService", data);
    if (stompClient == null) {return;}
    Log.i("OrderService", data);
    Disposable disposable =
        stompClient
            .send(destination, data)
            .compose(applySchedulers())
            .subscribe(
                () -> Log.i("OrderService", "send success"),
                throwable -> Log.e("OrderService", "send error", throwable));
    compositeDisposable.add(disposable);
  }

  public void createOrder(OrderModel model) {

    ObjectMapper mapObject = new ObjectMapper();
    Map <String, Object> map = mapObject.convertValue(model, new TypeReference<Map<String, Object>>(){});

    StompMessageModel messageModel = new StompMessageModel();
    messageModel.setOperationType(OperationType.CREATE_ORDER);
    messageModel.setPayload(map);


    String json = new Gson().toJson(messageModel);
    Log.i("OrderService", messageModel.toString());

    sendMessage(sendPath,json);
  }


}


