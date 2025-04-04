package com.github.philipepompeu.order_service.domains.model;

import java.io.Serializable;



import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="product")
public class ProductEntity extends BaseEntity implements Serializable {
    
    private String title;
    private String description;    

}
