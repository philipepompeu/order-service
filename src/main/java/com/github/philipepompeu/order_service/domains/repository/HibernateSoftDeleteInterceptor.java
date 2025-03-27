package com.github.philipepompeu.order_service.domains.repository;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

@Component
public class HibernateSoftDeleteInterceptor {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void enableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("softDeleteFilter");
    }
}
