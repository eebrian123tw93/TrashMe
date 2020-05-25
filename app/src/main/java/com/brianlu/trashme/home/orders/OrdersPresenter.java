package com.brianlu.trashme.home.orders;

import com.brianlu.trashme.api.consumer.ConsumerService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.dto.CustomResponse;
import com.brianlu.trashme.dto.PickupOrderInfo;
import com.brianlu.trashme.home.orders.adapter.OrdersRVAdapter;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class OrdersPresenter extends BasePresenter {

  private OrdersView view;
  private OrdersRVAdapter adapter;

  OrdersPresenter(OrdersView view) {
    this.view = view;
    adapter = new OrdersRVAdapter();
    view.setAdapter(adapter);
  }

  void getOrders() {
    ConsumerService.getInstance()
        .getAllOrders(false)
        .subscribe(
            new Observer<CustomResponse<List<PickupOrderInfo>>>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(CustomResponse<List<PickupOrderInfo>> customResponse) {
                List<PickupOrderInfo> models = customResponse.getPayload();
                System.out.println(models.size());
                adapter.addModels(models);
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
                view.onSetMessage(e.getMessage(), FancyToast.ERROR);
              }

              @Override
              public void onComplete() {}
            });
  }
}
