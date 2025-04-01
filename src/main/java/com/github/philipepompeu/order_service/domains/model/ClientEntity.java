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
@Table(name="client")
//@SQLDelete(sql = "UPDATE client SET revoked_at = CURRENT_TIMESTAMP WHERE id = ?")
public class ClientEntity extends BaseEntity implements Serializable {
    
    private String fullName;
    private String legalId;
    private String phoneNumber;
    private String email;    
}
