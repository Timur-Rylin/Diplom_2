package com.stellar.burgers.tests;

import com.stellar.burgers.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты создания пользователя")
public class CreateUserTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Позитивный тест: создание пользователя с уникальными данными")
    public void createUniqueUserSuccessfully() {
        String email = "testuser_" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Негативный тест: попытка создания пользователя, который уже существует")
    public void createExistingUserShouldFail() {
        String email = "existing_" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT);

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_USER_EXISTS));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Негативный тест: создание пользователя без заполнения поля email")
    public void createUserWithoutEmailShouldFail() {
        String requestBody = "{\"password\":\"password123\",\"name\":\"Test User\"}";

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Негативный тест: создание пользователя без заполнения поля password")
    public void createUserWithoutPasswordShouldFail() {
        String email = "test_" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format(
                "{\"email\":\"%s\",\"name\":\"Test User\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Негативный тест: создание пользователя без заполнения поля name")
    public void createUserWithoutNameShouldFail() {
        String email = "test_" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_REQUIRED_FIELDS));
    }
}