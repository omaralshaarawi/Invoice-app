package com.appsdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

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
class UsersWebServiceEndpointTree {

    private final String CONTEXT_PATH = "/invoice-app-ws";
    private final String EMAIL_ADDRESS = "sas@bin.com";
    private final String PASSWORD = "123";
    private final String JSON = "application/json";

    private static String authorizationHeader;
    private static String userId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    // a) Create User
    @Test
    @Order(1)
    final void a_createUser() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Omar");
        userDetails.put("lastName", "Shaarawi");
        userDetails.put("email", EMAIL_ADDRESS);
        userDetails.put("password", PASSWORD);

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .body(userDetails)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().response();

        userId = response.jsonPath().getString("userId");

        assertNotNull(userId);
        assertEquals(EMAIL_ADDRESS, response.jsonPath().getString("email"));
    }

    // b) Login User
    @Test
    @Order(2)
    final void b_loginUser() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL_ADDRESS);
        loginDetails.put("password", PASSWORD);

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
        String returnedUserId = response.header("userId");

        assertNotNull(authorizationHeader);
        assertEquals(userId, returnedUserId);
    }

    // c) Get User Details
    @Test
    @Order(3)
    final void c_getUserDetails() {
        Response response = given()
                .header("Authorization", authorizationHeader)
                .accept(JSON)
                .when()
                .get(CONTEXT_PATH + "/users/" + userId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        assertEquals(userId, response.jsonPath().getString("userId"));
        assertEquals(EMAIL_ADDRESS, response.jsonPath().getString("email"));
        assertNotNull(response.jsonPath().getString("firstName"));
        assertNotNull(response.jsonPath().getString("lastName"));
    }


}
