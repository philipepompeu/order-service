package com.github.philipepompeu.order_service.app.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.philipepompeu.order_service.app.dto.SaleOrderItemDto;
import com.github.philipepompeu.order_service.app.dto.SalesOrderDTO;
import com.github.philipepompeu.order_service.domains.model.ClientEntity;
import com.github.philipepompeu.order_service.domains.model.PaymentMethod;
import com.github.philipepompeu.order_service.domains.model.ProductEntity;
import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;
import com.github.philipepompeu.order_service.domains.repository.ClientRepository;
import com.github.philipepompeu.order_service.domains.repository.ProductRepository;
import com.github.philipepompeu.order_service.domains.repository.SaleOrderRepository;

import net.bytebuddy.utility.RandomString;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test of the class SalesOrderService")
public class SalesOrderServiceTest {

    @Mock
    private SaleOrderRepository salesOrderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PaymentObserver paymentObserver;

    @InjectMocks
    private SalesOrderService salesOrderService;


    private ProductEntity getMockProduct(){

        ProductEntity mockProduct = new ProductEntity();
        mockProduct.setDescription(String.format("Product %s", RandomString.make(10)));
        mockProduct.setId(UUID.randomUUID());
        mockProduct.setTitle(mockProduct.getDescription());

        return mockProduct;

    }

    @Test
    @DisplayName("should call notifyObserver/onSalesOrderCreated when a SalesOrder is created")
    void shouldNotifyObserverWhenSalesOrderIsCreated() {

        ClientEntity mockClient = new ClientEntity();
        mockClient.setId(UUID.randomUUID());
        mockClient.setLegalId("+55119936248899");
        mockClient.setFullName("Test Client");
        mockClient.setEmail("email@provider.com");
        mockClient.setLegalId("xpto");        

        when(clientRepository.findById(any())).thenReturn(Optional.of(mockClient));

        List<ProductEntity> mockProducts = List.of(getMockProduct(), getMockProduct(), getMockProduct());       

        when(productRepository.findByIdIn(any())).thenReturn(mockProducts);

        SalesOrderDTO dto = new SalesOrderDTO();
        dto.setClientId(mockClient.getId().toString());
        dto.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        dto.setFreightCost(BigDecimal.valueOf(100));        

        dto.setProducts(mockProducts.stream().map((entity)->{
            SaleOrderItemDto item = new SaleOrderItemDto();
            
            item.setQuantity(BigDecimal.valueOf(1));
            item.setPrice(BigDecimal.valueOf(10));
            item.setProductId(entity.getId().toString());

            return item;
        }).toList());  
        
        SaleOrderEntity mockSaleOrder = salesOrderService.convertDtoToEntity(dto); 
        mockSaleOrder.setId(UUID.randomUUID());   
        mockSaleOrder.setTotalValue(BigDecimal.valueOf(130));    

        mockSaleOrder.getItems().forEach(item ->{
            item.setId(UUID.randomUUID());
            item.setSaleOrder(mockSaleOrder);
        });        
        
        when(salesOrderRepository.save(any())).thenReturn(mockSaleOrder);

        salesOrderService.create(dto);

        verify(paymentObserver, times(1)).onSalesOrderCreated(any(SaleOrderEntity.class));
    }
    
}
