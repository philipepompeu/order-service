package com.github.philipepompeu.order_service.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.app.dto.ProductDto;
import com.github.philipepompeu.order_service.app.dto.SaleOrderItemDto;
import com.github.philipepompeu.order_service.app.dto.SalesOrderDTO;
import com.github.philipepompeu.order_service.domains.model.PaymentMethod;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.bytebuddy.utility.RandomString;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test E2E of the SalesOrderController(/v1/orders)")
public class SalesOrderControllerTest {


    private static final String ENDPOINT_URL = "/v1/orders";
    
    @LocalServerPort
    private int port;


    private String clientId;
    private List<String> listOfProductsIds;

    @BeforeEach
    void setup() {        
        baseURI = "http://localhost";

        if (clientId == null || clientId.isEmpty()) {
            this.clientId = getClientId();
        }
        if (listOfProductsIds == null) {
            listOfProductsIds = new ArrayList<String>();            
        }

        if (listOfProductsIds.isEmpty()) {
            
            listOfProductsIds.add(getProductId());
            listOfProductsIds.add(getProductId());
            listOfProductsIds.add(getProductId());

        }
    }

    String getProductId(){       
        
        String random = String.format("Product %s", RandomString.make(10));
        ProductDto product = new ProductDto();
        product.setTitle(random);
        product.setDescription(random);              

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(product)
                .with()
                    .port(port)
                .when()
                    .post("/v1/products");
        
        String productId = response.jsonPath().getString("id");

        return productId;
    }

    String getClientId(){

        ClientDTO client = new ClientDTO();
        client.setFullName("Client Full Name");
        client.setEmail("email@provider.com");
        client.setLegalId("9999900000");
        client.setPhoneNumber("+5511993632499");
        client.setId("id-to-be-ignored");         

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(client)
                .with()
                    .port(port)
                .when()
                    .post("/v1/clients");
        
        return response.jsonPath().getString("id");

    }

    List<SaleOrderItemDto> getSaleOrderItems(){

        return listOfProductsIds.stream()
                .map(id->  {

                    SaleOrderItemDto dto = new SaleOrderItemDto();
                    dto.setProductId(id);
                    dto.setQuantity(BigDecimal.valueOf(1));
                    
                    return dto;
                }).toList();
        
    }
    @Test
    void shouldCreateSaleOrderSuccessfully() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");        
        
        BigDecimal expectedTotalValue = BigDecimal.valueOf(100)
                                                  .multiply(BigDecimal.valueOf(order.getProducts().size()))
                                                  .add(order.getFreightCost());

        given()
            .contentType(ContentType.JSON)
            .body(order)
        .with().port(port)
        .when()
            .post(ENDPOINT_URL)
        .then()
            .statusCode(201)
            .body("id", notNullValue()) // Verifica se ID foi gerado
            .body("totalValue", equalTo(expectedTotalValue.floatValue()))
            .body("freightCost", equalTo(BigDecimal.valueOf(100).floatValue()))
            .body("clientId", equalTo(clientId));
    }
    
   
    @Test
    void shouldDeleteAExistingSalesOrder() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");           

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(order)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String orderId = response.jsonPath().getString("id");

        given()
            .with()
                .port(port)
            .when()
                .delete(ENDPOINT_URL+ "/" + orderId)
            .then()
                .statusCode(200)
                .body("id", equalTo(orderId));
    }

    
    @Test
    void shouldFailToDeleteNonExistentId() {               
        
        String clientId = UUID.randomUUID().toString();

        given()
            .with()
                .port(port)
            .when()
                .delete(ENDPOINT_URL+ "/" + clientId)
            .then()
                .statusCode(422);
                
    }
    
    @Test
    void shouldUpdateAExistingSalesOrder() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");     

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(order)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String orderId = response.jsonPath().getString("id");
        order.setId(orderId);
        order.setFreightCost(BigDecimal.valueOf(0));

        List<SaleOrderItemDto> productsWithIds =  response.jsonPath().getList("products", SaleOrderItemDto.class);
        order.setProducts(productsWithIds);
        order.getProducts().forEach(p -> p.setPrice(BigDecimal.valueOf(200)));        

        given()
                .contentType(ContentType.JSON)
                .body(order)
            .with()
                .port(port)
            .when()
                .put(ENDPOINT_URL+ "/" + orderId)
            .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("totalValue", equalTo(BigDecimal.valueOf(600.00).floatValue()));
    }

    
    @Test
    void shouldFailToUpdateNonExistentId() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");    
        
        String orderId = UUID.randomUUID().toString();

        order.setId(orderId);        

        given()
                .contentType(ContentType.JSON)
                .body(order)
            .with()
                .port(port)
            .when()
                .put(ENDPOINT_URL+ "/" + orderId)
            .then()
                .statusCode(422);
    }
    
    @Test
    void shouldReturnSaleOrderById() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");     

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(order)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String orderId = response.jsonPath().getString("id");

        given()                
            .with()
                .port(port)
            .when()
                .get(ENDPOINT_URL+ "/" + orderId)
            .then()
                .statusCode(200)
                .body("id", equalTo(orderId));
    }

    @Test
    void shouldReturnAllRecords() {
        SalesOrderDTO order = new SalesOrderDTO();

        order.setClientId(clientId);
        order.setFreightCost(BigDecimal.valueOf(100));
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);        
        order.setProducts(getSaleOrderItems());
        order.getProducts().forEach(product -> product.setPrice(BigDecimal.valueOf(100)));
        order.setId("id-to-be-ignored");       

        given()
                .contentType(ContentType.JSON)
                .body(order)
            .with()
                .port(port)
            .when()
                .post(ENDPOINT_URL)
            .then().statusCode(201);        
        

        given()
            .queryParam("page", 0)                
            .queryParam("size", 10)
            .with()
                .port(port)
            .when()
                .get(ENDPOINT_URL)
            .then()
                .statusCode(200)
                .body("content.size()", greaterThan(0))
                .body("numberOfElements", greaterThan(0));
    }

}
