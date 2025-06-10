package com.github.philipepompeu.order_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.app.services.ClientService;
import com.github.philipepompeu.order_service.domains.model.Address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/clients")
@Tag(name="Clients",
     description="List, create, delete and update clients for your orders.")
public class ClientController extends AbstractController<ClientDTO, UUID>{

    protected ClientController(ClientService service) {
        super(service);        
    }    

    @PostMapping("/{id}/address")
    @Operation(summary = "add a new address to existing client")
    public ResponseEntity<List<Address>> addNewAddress(@PathVariable UUID id, @RequestBody @Valid Address newAddress){        
        return ResponseEntity.ok().body(((ClientService)this.service).addAddress(id, newAddress));
    }

    @DeleteMapping("/{id}/address/{addressId}")
    @Operation(summary = "delete a existing address of the client")
    public ResponseEntity<List<Address>> addNewAddress(@PathVariable UUID id, @PathVariable @Valid UUID addressId){        
        return ResponseEntity.ok().body(((ClientService)this.service).removeAddress(id, addressId));
    }

    
    
}
