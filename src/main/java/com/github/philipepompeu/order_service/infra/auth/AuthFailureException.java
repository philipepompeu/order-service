package com.github.philipepompeu.order_service.infra.auth;

import jakarta.servlet.ServletException;

public class AuthFailureException extends ServletException {

    public AuthFailureException(String message) {
        super(message);
    }
}
