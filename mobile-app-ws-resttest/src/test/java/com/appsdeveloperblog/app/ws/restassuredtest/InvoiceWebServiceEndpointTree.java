package com.appsdeveloperblog.app.ws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InvoiceWebServiceEndpointTree {

    private final String CONTEXT_PATH = "/invoice-app-ws";
    private final String JSON = "application/json";
    
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    @Test
    void testCreateInvoice_Success() {
        Map<String, Object> invoiceRequest = new HashMap<>();
        invoiceRequest.put("providerName", "Test Provider");
        invoiceRequest.put("address", "Test Address");
        invoiceRequest.put("delivered_by", "John Doe");
        invoiceRequest.put("dateTime", LocalDateTime.now().toString());
        invoiceRequest.put("paid", 100L);
        invoiceRequest.put("total", 200L);
        invoiceRequest.put("remaining", 100L);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .body(invoiceRequest)
                .when()
                .post(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().response();

        String invoiceId = response.jsonPath().getString("invoiceId");
        assertNotNull(invoiceId);
        assertEquals("Test Provider", response.jsonPath().getString("providerName"));
        assertEquals("Test Address", response.jsonPath().getString("address"));
        assertEquals("John Doe", response.jsonPath().getString("delivered_by"));
    }

    @Test
    void testGetInvoices_Success() {
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .queryParam("page", 0)
                .queryParam("limit", 5)
                .when()
                .get(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(403)
                .contentType(JSON)
                .extract().response();

        assertNotNull(response.jsonPath().getList(""));
    }

    @Test
    void testGetInvoiceById_Success() {
        // First create invoice
        Map<String, Object> invoiceRequest = new HashMap<>();
        invoiceRequest.put("providerName", "Lookup Provider");
        invoiceRequest.put("address", "Lookup Address");
        invoiceRequest.put("delivered_by", "Jane Doe");
        invoiceRequest.put("dateTime", LocalDateTime.now().toString());
        invoiceRequest.put("paid", 150L);
        invoiceRequest.put("total", 300L);
        invoiceRequest.put("remaining", 150L);

        String invoiceId = given()
                .contentType(JSON)
                .accept(JSON)
                .body(invoiceRequest)
                .when()
                .post(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("invoiceId");

        // Fetch by ID
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .get(CONTEXT_PATH + "/invoice/" + invoiceId)
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("Lookup Provider", response.jsonPath().getString("providerName"));
    }

    @Test
    void testUpdateInvoice_Success() {
        // Create first
        Map<String, Object> invoiceRequest = new HashMap<>();
        invoiceRequest.put("providerName", "Old Provider");
        invoiceRequest.put("address", "Old Address");
        invoiceRequest.put("delivered_by", "Old Delivery");
        invoiceRequest.put("dateTime", LocalDateTime.now().toString());
        invoiceRequest.put("paid", 50L);
        invoiceRequest.put("total", 150L);
        invoiceRequest.put("remaining", 100L);

        String invoiceId = given()
                .contentType(JSON)
                .accept(JSON)
                .body(invoiceRequest)
                .when()
                .post(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("invoiceId");

        // Update it
        Map<String, Object> updatedInvoice = new HashMap<>();
        updatedInvoice.put("providerName", "Updated Provider");
        updatedInvoice.put("address", "Updated Address");
        updatedInvoice.put("delivered_by", "Updated Delivery");
        updatedInvoice.put("dateTime", LocalDateTime.now().toString());
        updatedInvoice.put("paid", 75L);
        updatedInvoice.put("total", 175L);
        updatedInvoice.put("remaining", 100L);

        Response updateResponse = given()
                .contentType(JSON)
                .accept(JSON)
                .body(updatedInvoice)
                .when()
                .put(CONTEXT_PATH + "/invoice/" + invoiceId)
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("Updated Provider", updateResponse.jsonPath().getString("providerName"));
        assertEquals("Updated Address", updateResponse.jsonPath().getString("address"));
    }

    @Test
    void testDeleteInvoice_Success() {
        // Create first
        Map<String, Object> invoiceRequest = new HashMap<>();
        invoiceRequest.put("providerName", "Delete Provider");
        invoiceRequest.put("address", "Delete Address");
        invoiceRequest.put("delivered_by", "Delete Delivery");
        invoiceRequest.put("dateTime", LocalDateTime.now().toString());
        invoiceRequest.put("paid", 50L);
        invoiceRequest.put("total", 150L);
        invoiceRequest.put("remaining", 100L);

        String invoiceId = given()
                .contentType(JSON)
                .accept(JSON)
                .body(invoiceRequest)
                .when()
                .post(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("invoiceId");

        // Delete it
        given()
                .when()
                .delete(CONTEXT_PATH + "/invoice/" + invoiceId)
                .then()
                .statusCode(403);
    }
}
