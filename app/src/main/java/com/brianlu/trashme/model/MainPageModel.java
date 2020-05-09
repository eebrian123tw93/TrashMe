package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class MainPageModel {
    double recycleTrashPrice;
    double normalTrashPrice;
    double mixedTrashPrice;
    String locationName;
    Double latitude;
    Double longitude;
    int pickupOrderTimes;
}
