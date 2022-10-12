package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class OrderModel {
  TrashType trashType;
  Double longitude;
  Double latitude;
  String locationName;
  String additionalInfo;
}
