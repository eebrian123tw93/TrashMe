package com.brianlu.trashme.home.orders;

import androidx.recyclerview.widget.RecyclerView;

import com.brianlu.trashme.base.BaseView;

public interface OrdersView extends BaseView {
  void setAdapter(RecyclerView.Adapter adapter);
}
