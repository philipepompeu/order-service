package com.github.philipepompeu.order_service.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.philipepompeu.order_service.app.dto.UserLoginDto;
import com.github.philipepompeu.order_service.app.services.LoginService;
import com.github.philipepompeu.order_service.infra.auth.AuthFailureException;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<String> create(@RequestBody UserLoginDto dto) throws AuthFailureException{

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(loginService.auth(dto));
    }
    
}
