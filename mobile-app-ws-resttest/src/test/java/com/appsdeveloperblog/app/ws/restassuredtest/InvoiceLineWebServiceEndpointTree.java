package com.appsdeveloperblog.app.ws.restassuredtest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InvoiceLineWebServiceEndpointTree {

    private final String CONTEXT_PATH = "/invoice-app-ws";
    private final String JSON = "application/json";

    private static String authorizationHeader;
    private static String invoiceId;
    private static String invoiceLineId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    // 1) Login to get token (assuming user already exists)
    @Test
    @Order(1)
    final void a_loginUser() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", "omar@RASN.com");
        loginDetails.put("password", "1234");

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH + "/users/login")
                .then()
                .statusCode(200)
                .extract().response();

        authorizationHeader = response.header("Authorization");
        assertNotNull(authorizationHeader);
    }

    // 2) Create Invoice (needed before adding invoice line)
    @Test
    @Order(2)
    final void b_createInvoice() {
        Map<String, Object> invoiceDetails = new HashMap<>();
        invoiceDetails.put("providerName", "Provider X");
        invoiceDetails.put("address", "123 Main St");
        invoiceDetails.put("delivered_by", "john");
        invoiceDetails.put("paid", 300L);

        Response response = given()
                .header("Authorization", authorizationHeader)
                .contentType(JSON)
                .accept(JSON)
                .body(invoiceDetails)
                .when()
                .post(CONTEXT_PATH + "/invoice")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().response();

        invoiceId = response.jsonPath().getString("invoiceId");

        assertNotNull(invoiceId);
        assertEquals("Provider X", response.jsonPath().getString("providerName"));
    }

 
}
