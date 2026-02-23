package com.stellar.burgers.tests;

import com.stellar.burgers.Constants;
import com.stellar.burgers.api.BaseTest;
import com.stellar.burgers.api.models.User;
import com.stellar.burgers.api.models.LoginResponse;
import com.stellar.burgers.api.models.ApiResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@DisplayName("Тесты создания пользователя")
public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Позитивный тест: создание пользователя с уникальными данными")
    public void createUniqueUserSuccessfully() {
        LoginResponse response = userClient.createUser(testUser)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", not(emptyOrNullString()))
                .body("refreshToken", not(emptyOrNullString()))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()))
                .extract()
                .as(LoginResponse.class);

        assertNotNull("Access token should not be null", response.getAccessToken());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Негативный тест: попытка создания пользователя, который уже существует")
    public void createExistingUserShouldFail() {
        ApiResponse response = userClient.createUser(testUser)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_USER_EXISTS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_USER_EXISTS, response.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Негативный тест: создание пользователя без заполнения поля email")
    public void createUserWithoutEmailShouldFail() {
        User userWithoutEmail = new User(null, "password123", "Test User");

        ApiResponse response = userClient.createUser(userWithoutEmail)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_REQUIRED_FIELDS, response.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Негативный тест: создание пользователя без заполнения поля password")
    public void createUserWithoutPasswordShouldFail() {
        User userWithoutPassword = new User("test@example.com", null, "Test User");

        ApiResponse response = userClient.createUser(userWithoutPassword)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_REQUIRED_FIELDS, response.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Негативный тест: создание пользователя без заполнения поля name")
    public void createUserWithoutNameShouldFail() {
        User userWithoutName = new User("test@example.com", "password123", null);

        ApiResponse response = userClient.createUser(userWithoutName)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_REQUIRED_FIELDS, response.getMessage());
    }
}