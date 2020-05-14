package com.brianlu.trashme.model;


import java.util.Map;

import lombok.Data;


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

