package com.brianlu.trashme.home.orders.adapter;

public interface OrdersVHView {

  void setTime(String time);

  void setStatus(String status);

  void setStatusColor(int color);

  void setType(String type);

  void setTypeImage(int resource);

  void setPickerUser(String pickerUser);

  void setWeight(String weight);
}
