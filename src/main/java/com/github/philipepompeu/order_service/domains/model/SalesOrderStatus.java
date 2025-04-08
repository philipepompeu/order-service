package com.github.philipepompeu.order_service.domains.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SalesOrderStatus {
    OPEN("Open"),
    AWAITING_PAYMENT("Awaiting Payment"),
    AWAITING_SHIPMENT("Awaiting Shipment"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered");

    private final String label;

    SalesOrderStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
    
}
