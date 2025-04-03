package com.github.philipepompeu.order_service.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.github.philipepompeu.order_service.app.dto.ClientDTO;
import com.github.philipepompeu.order_service.app.dto.ProductDto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test E2E of the ClientController(/v1/clients)")
public class ClientControllerTest {


    private static final String ENDPOINT_URL = "/v1/clients";
    
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {        
        baseURI = "http://localhost";
    }

    @Test
    void shouldCreateClientSuccessfully() {
        ClientDTO client = new ClientDTO();
        client.setFullName("Client Full Name");
        client.setEmail("email@provider.com");
        client.setLegalId("9999900000");
        client.setPhoneNumber("+5511993632499");
        client.setId("id-to-be-ignored");       

        given()
            .contentType(ContentType.JSON)
            .body(client)
        .with().port(port)
        .when()
            .post(ENDPOINT_URL)
        .then()
            .statusCode(201)
            .body("id", notNullValue()) // Verifica se ID foi gerado
            .body("legalId", equalTo("9999900000"))
            .body("phoneNumber", equalTo("+5511993632499"));
    }
    
    @Test
    void shouldDeleteAExistingClient() {
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
                    .post(ENDPOINT_URL);
        
        String clientId = response.jsonPath().getString("id");

        given()
            .with()
                .port(port)
            .when()
                .delete(ENDPOINT_URL+ "/" + clientId)
            .then()
                .statusCode(200)
                .body("id", equalTo(clientId));
    }
    
    @Test
    void shouldUpdateAExistingClient() {
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
                    .post(ENDPOINT_URL);
        
        String clientId = response.jsonPath().getString("id");

        client.setId(clientId);
        client.setEmail("updated@email.com");

        given()
                .contentType(ContentType.JSON)
                .body(client)
            .with()
                .port(port)
            .when()
                .put(ENDPOINT_URL+ "/" + clientId)
            .then()
                .statusCode(200)
                .body("id", equalTo(clientId))
                .body("email", equalTo("updated@email.com"));
    }
    
    @Test
    void shouldReturnClientById() {
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
                    .post(ENDPOINT_URL);
        
        String clientId = response.jsonPath().getString("id");

        given()                
            .with()
                .port(port)
            .when()
                .get(ENDPOINT_URL+ "/" + clientId)
            .then()
                .statusCode(200)
                .body("id", equalTo(clientId));
    }

    @Test
    void shouldReturnAllRecords() {
        ClientDTO client = new ClientDTO();
        client.setFullName("Client Full Name");
        client.setEmail("email@provider.com");
        client.setLegalId("9999900000");
        client.setPhoneNumber("+5511993632499");
        client.setId("id-to-be-ignored");      

        given()
                .contentType(ContentType.JSON)
                .body(client)
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
