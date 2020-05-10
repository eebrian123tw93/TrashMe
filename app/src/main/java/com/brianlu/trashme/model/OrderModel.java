package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class OrderModel {
    TrashType trashType;
    double longitude;
    double latitude;
    String locationName;
    String additionalInfo;
}

enum TrashType {
    NORMAL("NORMAL"),
    MIX("MIX"),
    RECYCLE("RECYCLE");



    TrashType(String normal) {
    }
}
