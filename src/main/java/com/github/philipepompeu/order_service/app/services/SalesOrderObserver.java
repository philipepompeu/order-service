package com.github.philipepompeu.order_service.app.services;

import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

public interface SalesOrderObserver {
    void onSalesOrderUpdated(SaleOrderEntity salesOrder);
    void onSalesOrderCreated(SaleOrderEntity salesOrder);
    void onSalesOrderDeleted(SaleOrderEntity salesOrder);    
}
