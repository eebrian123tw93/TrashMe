package com.brianlu.trashme.model;

public enum OperationType {
  SERVER_CREATED_ORDER("SERVER_CREATED_ORDER"),
  SERVER_ACCEPTED_ORDER("SERVER_ACCEPTED_ORDER"),
  LOCATION_UPDATE("LOCATION_UPDATE"),
  SERVER_FINISHED_ORDER("SERVER_FINISHED_ORDER"),
  CREATE_ORDER("CREATE_ORDER"),
  OTHER("OTHER");
  OperationType(String normal) {}
}
