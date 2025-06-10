package com.github.philipepompeu.order_service.domains.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;

import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="client")
public class ClientEntity extends BaseEntity implements Serializable {
    
    private String fullName;
    private String legalId;
    private String phoneNumber;
    private String email;    

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_addresses", joinColumns = @JoinColumn(name = "client_id"))
    private List<Address> addresses;

    public ClientEntity(){
        this.addresses = new ArrayList<Address>();
    }
}
