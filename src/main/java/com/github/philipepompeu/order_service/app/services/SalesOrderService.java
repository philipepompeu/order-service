package com.github.philipepompeu.order_service.app.services;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger log = LoggerFactory.getLogger(SalesOrderService.class);

    private final SaleOrderRepository repository;

    private final ProductRepository productRepository;
    
    private final ClientRepository clientRepository;

    private final List<SalesOrderObserver> observers = new ArrayList<>();

    public SalesOrderService(SaleOrderRepository repository,ProductRepository productRepository,ClientRepository clientRepository, PaymentObserver paymentObserver){

        this.repository = repository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;

        observers.add(paymentObserver);
    }

    private void notifyObservers(SaleOrderEntity salesOrder, String operation) {       

        for (SalesOrderObserver observer : observers) {
            try {
                observer.getClass().getMethod(operation, SaleOrderEntity.class).invoke(observer,salesOrder);
            } catch (Exception e) {                
                log.error("NotifyObserver error"+e.getMessage());           
            }            
        }
    }

    @Override
    public Optional<SalesOrderDTO> getById(UUID id) {
        return repository.findById(id).map(entity -> new SalesOrderDTO(entity));
    }

    public SaleOrderEntity convertDtoToEntity(SalesOrderDTO dto){

        SaleOrderEntity entity;
        if (dto.getId() != null && !dto.getId().isEmpty() && !dto.getId().isBlank() ) {
            UUID id = UUID.fromString(dto.getId());
            entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Order id [%s] not found", id.toString())));
            entity.setUpdatedAt(LocalDateTime.now());//caso apenas os itens sofram alterações, força atualização da entidade SaleOrderEntity, dessa forma atualizando o totalizador
        }else{
            entity = new SaleOrderEntity();
        }

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
        

        if (entity.getItems().isEmpty()) {
            entity.setItems(items);            
        }else{
            List<UUID> receivedItemIds  = items.stream().filter(it-> it.getId() != null).map(it -> it.getId()).toList();

            entity.getItems().removeIf(it -> !receivedItemIds.contains(it.getId()) ); //Remove os itens que não estiverem no update
            
            for(SaleOrderItem it :(Iterable<SaleOrderItem>) items.stream()::iterator){
                if (it.getId() == null) {
                    entity.getItems().add(it); //Adiciona os novos itens
                }else if(entity.getItems().contains(it)){
                    SaleOrderItem currentItem = entity.getItems().get(  entity.getItems().indexOf(it) );
                    currentItem.setPrice(it.getPrice());
                    currentItem.setQuantity(it.getQuantity());
                    currentItem.setProduct(it.getProduct());
                }
            }          
                       
        }

        return entity;

    }

    @Override
    public SalesOrderDTO create(SalesOrderDTO dto) {

        dto.setId(null);//garante que vai criar um novo registro

        SaleOrderEntity entity = convertDtoToEntity(dto); 

        dto = new SalesOrderDTO(repository.save(entity));
        
        notifyObservers(entity, "onSalesOrderCreated");

        return dto;
        
    }

    @Override
    public Page<SalesOrderDTO> getAll(Pageable pageable) {        
        return repository.findAll(pageable).map(entity-> new SalesOrderDTO(entity));
    }

    @Override
    public SalesOrderDTO update(UUID id, SalesOrderDTO dto) {
        
        dto.setId(id.toString());
        SaleOrderEntity entity = convertDtoToEntity(dto);

        notifyObservers(entity, "onSalesOrderUpdated");

        return new SalesOrderDTO(repository.save(entity));
    }

    @Override
    public SalesOrderDTO delete(UUID id) {
        SaleOrderEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Order id [%s] not found", id.toString())));
        
        repository.delete(entity);

        notifyObservers(entity, "onSalesOrderDeleted");

        return new SalesOrderDTO(entity);
    }
    
}
