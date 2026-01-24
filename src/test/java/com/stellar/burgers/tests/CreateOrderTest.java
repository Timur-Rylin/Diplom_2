package com.stellar.burgers.tests;

import com.stellar.burgers.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты создания заказа")
public class CreateOrderTest {

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Позитивный тест: создание заказа авторизованным пользователем с валидными ингредиентами")
    public void createOrderWithAuthorizationSuccessfully() {
        String email = "orderuser_" + System.currentTimeMillis() + "@example.com";
        String userBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        String token = given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(userBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String orderBody = String.format(
                "{\"ingredients\":[\"%s\",\"%s\"]}",
                Constants.VALID_INGREDIENTS[0],
                Constants.VALID_INGREDIENTS[1]
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(orderBody)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Негативный тест: создание заказа без токена авторизации")
    public void createOrderWithoutAuthorizationShouldFail() {
        String orderBody = String.format(
                "{\"ingredients\":[\"%s\",\"%s\"]}",
                Constants.VALID_INGREDIENTS[0],
                Constants.VALID_INGREDIENTS[1]
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(orderBody)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_NOT_AUTHORIZED));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Позитивный тест: создание заказа с валидными ингредиентами")
    public void createOrderWithIngredientsSuccessfully() {
        String email = "orderuser2_" + System.currentTimeMillis() + "@example.com";
        String userBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        String token = given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(userBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String orderBody = String.format(
                "{\"ingredients\":[\"%s\",\"%s\"]}",
                Constants.VALID_INGREDIENTS[0],
                Constants.VALID_INGREDIENTS[1]
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(orderBody)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Негативный тест: создание заказа с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsShouldFail() {
        String email = "orderuser3_" + System.currentTimeMillis() + "@example.com";
        String userBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        String token = given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(userBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String orderBody = "{\"ingredients\":[]}";

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(orderBody)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_NO_INGREDIENTS));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Негативный тест: создание заказа с невалидными ID ингредиентов")
    public void createOrderWithInvalidIngredientHashShouldFail() {
        String email = "orderuser4_" + System.currentTimeMillis() + "@example.com";
        String userBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        String token = given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(userBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        String orderBody = String.format(
                "{\"ingredients\":[\"%s\",\"another_invalid_hash\"]}",
                Constants.INVALID_INGREDIENT_HASH
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(orderBody)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(500);
    }
}