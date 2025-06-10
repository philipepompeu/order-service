package com.github.philipepompeu.order_service.app.services;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID> {
    
    public Optional<T> getById(ID id);
    public T create(T dto);
    public Page<T> getAll(Pageable pageable);
    public T update(ID id, T dto);
    public T delete(ID id);
}
