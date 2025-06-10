package com.github.philipepompeu.order_service.app.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.philipepompeu.order_service.domains.model.ProductEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    
    @NotBlank
    private String title;

    @NotBlank
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
