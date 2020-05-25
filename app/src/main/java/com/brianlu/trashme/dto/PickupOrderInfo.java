package com.brianlu.trashme.dto;

import com.brianlu.trashme.model.TrashType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickupOrderInfo {

  private String orderId;
  private String status;

//  private LocalDateTime orderTime;
  private String orderTime;
  private TrashType trashType;
  private Double trashWeight;
  private String pickupUser;
//  private LocalDateTime pickupTime;
  private String pickupTime;
}
