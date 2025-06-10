package com.github.philipepompeu.order_service.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.philipepompeu.order_service.app.services.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

public abstract class AbstractController<T, ID> {

    protected final BaseService<T, ID> service;

    protected AbstractController(BaseService<T, ID> service) {
        this.service = service;
    }

    @PostMapping()
    @Operation(summary = "create a new record")
    public ResponseEntity<T> create(@RequestBody @Valid T dto){
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(service.create(dto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ADMIN')")
    @Operation(summary = "delete a existing record with the id provided")
    public ResponseEntity<?> delete(@PathVariable ID id){        
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(service.delete(id));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "update a existing record with the id provided")  
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody @Valid T dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    @Operation(summary = "list all the records")
    public ResponseEntity<Page<T>> list(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size){
        
                                            
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.of(Optional.of(service.getAll(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "return a record with the provided id")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return ResponseEntity.of(service.getById(id));
    }
    
}
