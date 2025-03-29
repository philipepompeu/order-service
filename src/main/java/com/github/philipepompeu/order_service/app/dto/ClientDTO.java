package com.github.philipepompeu.order_service.app.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.philipepompeu.order_service.domains.model.ClientEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String fullName;
    private String legalId;
    private String phoneNumber;
    private String email;
    
    public ClientDTO(ClientEntity entity){

        this.fullName = entity.getFullName();
        this.legalId = entity.getLegalId();
        this.phoneNumber = entity.getPhoneNumber();
        this.email = entity.getEmail();

        if (entity.getId() != null) {
            this.id = entity.getId().toString();
        }
    }

    public ClientEntity toEntity(){
        var entity = new ClientEntity();

        entity.setFullName(fullName);
        entity.setLegalId(legalId);
        entity.setPhoneNumber(phoneNumber);
        entity.setEmail(email);

        if (id != null) {
            entity.setId(UUID.fromString(id));
        }

        return entity;

    }
}
