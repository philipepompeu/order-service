package com.github.philipepompeu.order_service.app.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.philipepompeu.order_service.domains.model.ClientEntity;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    
    @NonNull
    @NotBlank
    private String fullName;
    
    @NotBlank
    private String legalId;
    
    @NonNull
    private String phoneNumber;

    @NonNull
    @Email
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
