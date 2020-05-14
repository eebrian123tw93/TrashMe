package com.brianlu.trashme.model;


import java.util.Map;

import lombok.Data;

enum OperationType {
  SERVER_CREATED_ORDER("SERVER_CREATED_ORDER"),
  SERVER_ACCEPTED_ORDER("SERVER_ACCEPTED_ORDER"),
  LOCATION_UPDATE("LOCATION_UPDATE"),
  SERVER_FINISHED_ORDER("SERVER_FINISHED_ORDER"),
  OTHER("OTHER");
  OperationType(String normal) {}
}


@Data
public class StompMessageModel {
  private OperationType operationType;
  private Map<String, Object> payload;

  public void checkPayLoadIsNotNuLL() throws Exception {
    if (payload == null || payload.isEmpty()) {
      throw new Exception("payload is empty");
    }
  }
}

