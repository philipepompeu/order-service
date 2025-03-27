package com.github.philipepompeu.order_service.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.github.philipepompeu.order_service.app.services.SalesOrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name="Orders",
     description="List, create, delete and update sales orders.")
public class SalesOrderController extends AbstractController{

    protected SalesOrderController(SalesOrderService service) {
        super(service);        
    }
    
    

}
