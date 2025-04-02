package com.github.philipepompeu.order_service.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.app.services.ClientService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/clients")
@Tag(name="Clients",
     description="List, create, delete and update clients for your orders.")
public class ClientController extends AbstractController<ClientDTO, UUID>{

    protected ClientController(ClientService service) {
        super(service);        
    }    

    
    
}
