package com.github.philipepompeu.order_service.domains.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {    

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;    

    @JsonIgnore
    private LocalDateTime createdAt;

    @NotBlank
    private String street;         // Logradouro
    
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Special characters are not allowed.")
    private String number;         // Número
    private String complement;     // Complemento (opcional)
    
    @NotBlank
    private String neighborHood;   // Bairro
    
    @NotBlank
    private String city;           // Cidade
    private BrazilianState state;  // Estado (sigla)
    
    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Invalid postal code format. Expected format is 99999-999.")
    private String postalCode;     // CEP

    public Address(){
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }
}
