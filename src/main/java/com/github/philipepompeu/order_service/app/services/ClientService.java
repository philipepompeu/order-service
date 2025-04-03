package com.github.philipepompeu.order_service.app.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.domains.model.ClientEntity;
import com.github.philipepompeu.order_service.domains.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService implements BaseService<ClientDTO, UUID> {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository){
        this.repository = repository;
    }

    @Override
    public Optional<ClientDTO> getById(UUID id) {
        return repository.findById(id).map(op -> new ClientDTO(op) );
    }

    @Override
    public ClientDTO create(ClientDTO dto) {
        var entity = dto.toEntity();

        entity = repository.save(entity);

        return new ClientDTO(entity);
    }

    @Override
    public Page<ClientDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(entity -> new ClientDTO(entity));   
    }

    @Override
    public ClientDTO update(UUID id, ClientDTO dto) {
        ClientEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Client with id [ %s ] not found.", id.toString())));

        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setLegalId(dto.getLegalId());
        entity.setPhoneNumber(dto.getPhoneNumber());
        
        entity = repository.save(entity);
        
        return new ClientDTO(entity);
        
    }

    @Override
    public ClientDTO delete(UUID id) {
        var entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Client with id [ %s ] not found.", id.toString())));

        ClientDTO dto = new ClientDTO(entity);
        
        repository.delete(entity);
        
        return dto;
    }
    
}
