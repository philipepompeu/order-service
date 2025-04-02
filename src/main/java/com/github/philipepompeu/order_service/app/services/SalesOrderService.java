package com.github.philipepompeu.order_service.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.philipepompeu.order_service.app.dto.SalesOrderDTO;
import com.github.philipepompeu.order_service.domains.model.ClientEntity;
import com.github.philipepompeu.order_service.domains.model.ProductEntity;
import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;
import com.github.philipepompeu.order_service.domains.model.SaleOrderItem;
import com.github.philipepompeu.order_service.domains.repository.ClientRepository;
import com.github.philipepompeu.order_service.domains.repository.ProductRepository;
import com.github.philipepompeu.order_service.domains.repository.SaleOrderRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SalesOrderService implements BaseService<SalesOrderDTO, UUID>{

    @Autowired
    private SaleOrderRepository repository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Optional<SalesOrderDTO> getById(UUID id) {
        return repository.findById(id).map(entity -> new SalesOrderDTO(entity));
    }

    @Override
    public SalesOrderDTO create(SalesOrderDTO dto) {     
        
        
        ClientEntity client = clientRepository.findById(UUID.fromString(dto.getClientId()))
                                .orElseThrow(() -> new EntityNotFoundException(String.format("Client with id [ %s ] not found.", dto.getClientId())));
        
        List<UUID> productIds = dto.getProducts().stream().map(p -> UUID.fromString(p.getProductId()) ).toList();

        List<ProductEntity> products = productRepository.findByIdIn(productIds);        

        if (products.isEmpty()) {
            String listOfIds = productIds.stream().map(id-> id.toString()).collect(Collectors.joining(","));            
            throw new EntityNotFoundException(String.format("Product with id [ %s ] not found.", listOfIds));
        }

        // Criar um Map para evitar buscas repetitivas
        Map<UUID, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, p -> p));
        
        SaleOrderEntity entity = new SaleOrderEntity();
        entity.setClient(client);
        entity.setFreightCost(dto.getFreightCost());        
        entity.setPaymentMethod(dto.getPaymentMethod());        

        List<SaleOrderItem> items = dto.getProducts().stream()
                                        .map(saleItemDto -> {
                                            ProductEntity product = productMap.get(UUID.fromString(saleItemDto.getProductId()));
                                            if (product == null) {                
                                                throw new EntityNotFoundException(String.format("Product with id [ %s ] not found.", saleItemDto.getProductId()));
                                            }

                                            return new SaleOrderItem(saleItemDto, entity, product);
                                        }).toList();        
        entity.setItems(items);
        
        return new SalesOrderDTO(repository.save(entity));        
        
    }

    @Override
    public Page<SalesOrderDTO> getAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public SalesOrderDTO update(UUID id, SalesOrderDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public SalesOrderDTO delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
