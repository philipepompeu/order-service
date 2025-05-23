package com.github.philipepompeu.order_service.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.github.philipepompeu.order_service.app.dto.ProductDto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test E2E of the ProductController(/v1/products)")
public class ProductControllerTest {


    private static final String ENDPOINT_URL = "/v1/products";
    
    @LocalServerPort
    private int port;

    private String jwtToken;

    @BeforeEach
    void setup() {        
        baseURI = "http://localhost";

        Response response = given()
        .contentType(ContentType.JSON)
        .body(Map.of("username", "admin", "password", "123"))
        .port(port)
        .when()
            .post("/login");

        jwtToken = response.body().asString();
    }

    RequestSpecification authenticatedRequest() {
        return given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + jwtToken)
            .with().port(port);
    }


    @Test
    void shouldCreateProductSuccessfully() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        authenticatedRequest()
            .body(product)
            .when()
            .post(ENDPOINT_URL)
        .then()
            .statusCode(201)
            .body("id", notNullValue()) // Verifica se ID foi gerado
            .body("title", equalTo("PS5 Slim"))
            .body("description", equalTo("Sony Playstation 5 Slim"));
    }
    
    @Test
    void shouldFailForLackOfAuthToken() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        given()
            .contentType(ContentType.JSON)
            .body(product)
        .with().port(port)            
            .when()
            .post(ENDPOINT_URL)
        .then()
        .statusCode(401);
    }
    
    @Test
    void shouldDeleteAExistingProduct() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        Response response = authenticatedRequest()
                    .body(product)
                    .when()
                    .post(ENDPOINT_URL);
        
        String productId = response.jsonPath().getString("id");

        authenticatedRequest()
            .when()
                .delete(ENDPOINT_URL+ "/" + productId)
            .then()
                .statusCode(200)
                .body("id", equalTo(productId));
    }
    
    @Test
    void shouldUpdateAExistingProduct() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        Response response = authenticatedRequest().body(product)
                            .when()                            
                                .post(ENDPOINT_URL);        
        String productId = response.jsonPath().getString("id");

        product.setId(productId);
        product.setTitle("Updated Title");

        authenticatedRequest().body(product)
        .when()
            .put(ENDPOINT_URL+ "/" + productId)
        .then()
            .statusCode(200)
            .body("id", equalTo(productId))
            .body("title", equalTo("Updated Title"));
                
            
    }
    
    @Test
    void shouldReturnProductById() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        Response response = authenticatedRequest().body(product)
                            .when()                            
                                .post(ENDPOINT_URL);        
        String productId = response.jsonPath().getString("id");

        authenticatedRequest()
        .when()
            .get(ENDPOINT_URL+ "/" + productId)
        .then()
            .statusCode(200)
            .body("id", equalTo(productId));
    }

    @Test
    void shouldReturnAllRecords() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        authenticatedRequest().body(product)                
            .when()
                .post(ENDPOINT_URL)
            .then().statusCode(201);
        authenticatedRequest()
            .queryParam("page", 0)                
            .queryParam("size", 10)
            .when()
                .get(ENDPOINT_URL)
            .then()
                .statusCode(200)
                .body("content.size()", greaterThan(0))
                .body("page.totalElements", greaterThan(0));
    }

}
