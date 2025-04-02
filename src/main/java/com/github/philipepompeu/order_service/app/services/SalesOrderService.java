package com.github.philipepompeu.order_service.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public SalesOrderDTO create(SalesOrderDTO dto) {     
        
        
        ClientEntity client = clientRepository.findById(UUID.fromString(dto.getClientId())).orElseThrow();        
        
        List<ProductEntity> products = productRepository.findByIdIn(dto.getProducts().stream().map(p -> UUID.fromString(p.getProductId()) ).toList()).orElseThrow();

        List<SaleOrderItem> items = new ArrayList<SaleOrderItem>();
        
        SaleOrderEntity entity = new SaleOrderEntity();
        entity.setClient(client);
        entity.setFreightCost(dto.getFreightCost());        
        entity.setPaymentMethod(dto.getPaymentMethod());
        
        dto.getProducts().forEach(saleItemDto -> {
            ProductEntity product = products.stream().filter(p-> p.getId().toString().equals(saleItemDto.getProductId())).findFirst().orElseThrow();

            SaleOrderItem item = new SaleOrderItem();
            
            item.setProduct(product);
            item.setQuantity(saleItemDto.getQuantity());
            item.setPrice(saleItemDto.getPrice());
            item.setSaleOrder(entity);            
            items.add(item);
        });
        
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
