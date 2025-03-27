package com.github.philipepompeu.order_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Tag(name="Products",
     description="List, create, delete and update products for your orders.")
public class ProductController {

    @PostMapping()
    @Operation(summary = "...")
    public ResponseEntity<?> create(){
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "...")
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok().build();
    }
    
    @PutMapping
    @Operation(summary = "...")
    public ResponseEntity<?> update(){
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "...")
    public ResponseEntity<List<?>> list(){        
        return ResponseEntity.ok().build();
    }
    
}
