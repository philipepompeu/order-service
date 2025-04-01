package com.github.philipepompeu.order_service.domains.model;

import java.io.Serializable;

import org.hibernate.annotations.SQLDelete;

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
//@SQLDelete(sql = "UPDATE product SET revoked_at = CURRENT_TIMESTAMP WHERE id = ?")
public class ProductEntity extends BaseEntity implements Serializable {
    
    private String title;
    private String description;    

}
