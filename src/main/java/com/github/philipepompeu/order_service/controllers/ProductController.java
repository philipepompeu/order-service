package com.github.philipepompeu.order_service.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.philipepompeu.order_service.app.dto.ProductDto;
import com.github.philipepompeu.order_service.app.services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/products")
@Tag(name="Products",
     description="List, create, delete and update products for your orders.")
public class ProductController extends AbstractController<ProductDto, UUID>{

    public ProductController(ProductService service) {
        super(service);        
    }

    
    
}
