package com.github.philipepompeu.order_service.app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.domains.model.Address;
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

    public List<Address> addAddress(UUID id, Address newAddress) {
        ClientEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Client with id [ %s ] not found.", id.toString())));

        entity.getAddresses().add(newAddress);
        
        entity = repository.save(entity);       
        
        return entity.getAddresses();
        
    }
    
    public List<Address> removeAddress(UUID clientId, UUID addressId) {
        ClientEntity entity = repository.findById(clientId).orElseThrow(()-> new EntityNotFoundException(String.format("Client with id [ %s ] not found.", clientId.toString())));

        List<Address> addresses = entity.getAddresses();
        
        IntStream.range(0, addresses.size())
            .filter(i -> addressId.equals(addresses.get(i).getId()))
            .findFirst()
            .ifPresent(i -> addresses.remove(i));
        
        entity = repository.save(entity);       
        
        return entity.getAddresses();
        
    }
    
}
