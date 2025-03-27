package com.github.philipepompeu.order_service.app.dto;

import java.util.UUID;

import com.github.philipepompeu.order_service.domains.model.ProductEntity;

import lombok.Data;

@Data
public class ProductDto {

    private String id;
    private String title;
    private String description; 

    public ProductEntity toEntity(){
        ProductEntity entity = new ProductEntity();

        entity.setDescription(this.getDescription());
        entity.setTitle(this.getTitle());
        if (this.getId() != null) {
            entity.setId(UUID.fromString(this.getId()));
        }
        return entity;
    }
    
    public ProductDto(ProductEntity entity){        

        this.setDescription(entity.getDescription());
        this.setTitle(entity.getTitle());
        if (entity.getId() != null) {
            this.setId(entity.getId().toString());
        }       
        
    }
}
