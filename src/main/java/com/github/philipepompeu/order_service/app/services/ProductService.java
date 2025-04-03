package com.github.philipepompeu.order_service.app.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.philipepompeu.order_service.app.dto.ProductDto;
import com.github.philipepompeu.order_service.domains.model.ProductEntity;
import com.github.philipepompeu.order_service.domains.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ProductService implements BaseService<ProductDto, UUID> {
    
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProductDto> getById(UUID id) {
        return repository.findById(id).map(op -> new ProductDto(op) );
    }

    @Override
    public ProductDto create(ProductDto dto) {
        ProductEntity entity = dto.toEntity();

        entity = repository.save(entity);

        return new ProductDto(entity);
    }

    @Override
    public Page<ProductDto> getAll(Pageable pageable) {

        return repository.findAll(pageable).map(entity -> new ProductDto(entity));        
        
    }

    @Override
    public ProductDto update(UUID id, ProductDto dto) {

        ProductEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Product with id [ %s ] not found.", id.toString())));

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        entity = repository.save(entity);        
        return new ProductDto(entity);
    }

    @Override
    public ProductDto delete(UUID id) {
        
        ProductEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Product with id [ %s ] not found.", id.toString())));

        ProductDto dto = new ProductDto(entity);
        
        repository.delete(entity);
        
        return dto;
    }
    
    
}
