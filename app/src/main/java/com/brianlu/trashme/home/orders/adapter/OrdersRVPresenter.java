package com.brianlu.trashme.home.orders.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.brianlu.trashme.R;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.dto.PickupOrderInfo;

import java.util.ArrayList;
import java.util.List;

class OrdersRVPresenter extends BasePresenter {

  private List<PickupOrderInfo> models;

  OrdersRVPresenter() {
    models = new ArrayList<>();
  }

  void onBindViewHolder(@NonNull OrdersRVAdapter.ViewHolder viewHolder, int position) {
    PickupOrderInfo model = models.get(position);
    viewHolder.setPickerUser(model.getPickupUser());
    viewHolder.setWeight(String.format("%.1f", model.getTrashWeight()) + "kg");

    String status = model.getStatus();
    viewHolder.setStatus(status);
    if (status.equals("FINISHED")) {
      viewHolder.setStatusColor(Color.parseColor("#76BA1B"));
      viewHolder.setTime(model.getPickupTime());
    } else if (status.equals("ACCEPTED") || status.equals("WAITING")) {
      viewHolder.setStatusColor(Color.parseColor("#B7B0B1"));
      viewHolder.setTime(model.getOrderTime());
    } else {
      viewHolder.setStatusColor(Color.parseColor("#d11a2a"));
      viewHolder.setTime(model.getOrderTime());
    }

    switch (model.getTrashType()) {
      case NORMAL:
        viewHolder.setType("一般垃圾");
        viewHolder.setTypeImage(R.drawable.trash);
        break;
      case MIXED:
        viewHolder.setType("混合");
        viewHolder.setTypeImage(R.drawable.mix);
        break;
      case RECYCLE:
        viewHolder.setType("資源回收");
        viewHolder.setTypeImage(R.drawable.recycle);
        break;
    }
  }

  int getItemCount() {
    return models.size();
  }

  void addModel(PickupOrderInfo model) {
    models.add(model);
  }

  void addModels(List<PickupOrderInfo> models) {
    this.models.addAll(models);
  }

  void clearAll() {
    models.clear();
  }
}
