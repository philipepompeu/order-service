package com.github.philipepompeu.order_service.domains.repository;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class SoftDeleteFilter implements Filter{

    private final HibernateSoftDeleteInterceptor hibernateSoftDeleteInterceptor;

    public SoftDeleteFilter(HibernateSoftDeleteInterceptor hibernateSoftDeleteInterceptor) {
        this.hibernateSoftDeleteInterceptor = hibernateSoftDeleteInterceptor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        hibernateSoftDeleteInterceptor.enableSoftDeleteFilter();
        chain.doFilter(request, response);
    }
    
}
