package com.github.philipepompeu.order_service.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.github.philipepompeu.order_service.app.dto.ProductDto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test E2E of the ProductController(/v1/products)")
public class ProductControllerTest {


    private static final String ENDPOINT_URL = "/v1/products";
    
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {        
        baseURI = "http://localhost";        
    }


    @Test
    void shouldCreateProductSuccessfully() {
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
            .statusCode(201)
            .body("id", notNullValue()) // Verifica se ID foi gerado
            .body("title", equalTo("PS5 Slim"))
            .body("description", equalTo("Sony Playstation 5 Slim"));
    }
    
    @Test
    void shouldDeleteAExistingProduct() {
        ProductDto product = new ProductDto();
        product.setTitle("PS5 Slim");
        product.setDescription("Sony Playstation 5 Slim");
        product.setId("id-to-be-ignored");       

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(product)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String productId = response.jsonPath().getString("id");

        given()
            .with()
                .port(port)
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

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(product)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String productId = response.jsonPath().getString("id");

        product.setId(productId);
        product.setTitle("Updated Title");

        given()
                .contentType(ContentType.JSON)
                .body(product)
            .with()
                .port(port)
            .when()
                .put("/v1/products/" + productId)
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

        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(product)
                .with()
                    .port(port)
                .when()
                    .post(ENDPOINT_URL);
        
        String productId = response.jsonPath().getString("id");

        given()                
            .with()
                .port(port)
            .when()
                .get("/v1/products/" + productId)
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

        given()
                .contentType(ContentType.JSON)
                .body(product)
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
