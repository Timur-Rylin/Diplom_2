package com.stellar.burgers.tests;

import com.stellar.burgers.Constants;
import com.stellar.burgers.api.BaseTest;
import com.stellar.burgers.api.models.Order;
import com.stellar.burgers.api.models.OrderResponse;
import com.stellar.burgers.api.models.ApiResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@DisplayName("Тесты создания заказа")
public class CreateOrderTest extends BaseTest {

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Позитивный тест: создание заказа авторизованным пользователем с валидными ингредиентами")
    public void createOrderWithAuthorizationSuccessfully() {
        Order order = new Order(Arrays.asList(Constants.VALID_INGREDIENTS));

        OrderResponse response = orderClient.createOrder(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", not(emptyOrNullString()))
                .body("order.number", greaterThan(0))
                .extract()
                .as(OrderResponse.class);

        assertTrue("Order should be successful", response.isSuccess());
        assertNotNull("Order number should be present", response.getOrder());
        assertTrue("Order number should be positive", response.getOrder().getNumber() > 0);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Негативный тест: создание заказа без токена авторизации")
    public void createOrderWithoutAuthorizationShouldFail() {
        Order order = new Order(Arrays.asList(Constants.VALID_INGREDIENTS));

        ApiResponse response = orderClient.createOrderWithoutAuth(order)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_NOT_AUTHORIZED))
                .extract()
                .as(ApiResponse.class);

        assertFalse("Response should indicate failure", response.isSuccess());
        assertEquals("Error message should match", Constants.ERROR_NOT_AUTHORIZED, response.getMessage());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Позитивный тест: создание заказа с валидными ингредиентами")
    public void createOrderWithIngredientsSuccessfully() {
        Order order = new Order(Arrays.asList(Constants.VALID_INGREDIENTS));

        OrderResponse response = orderClient.createOrder(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order", notNullValue())
                .extract()
                .as(OrderResponse.class);

        assertTrue("Order should be successful", response.isSuccess());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Негативный тест: создание заказа с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsShouldFail() {
        Order emptyOrder = new Order(Collections.emptyList());

        ApiResponse response = orderClient.createOrder(emptyOrder, accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_NO_INGREDIENTS))
                .extract()
                .as(ApiResponse.class);

        assertFalse("Response should indicate failure", response.isSuccess());
        assertEquals("Error message should match", Constants.ERROR_NO_INGREDIENTS, response.getMessage());
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Негативный тест: создание заказа с невалидными ID ингредиентов")
    public void createOrderWithInvalidIngredientHashShouldFail() {
        Order invalidOrder = new Order(Arrays.asList(Constants.INVALID_INGREDIENT_HASH, "another_invalid_hash"));

        orderClient.createOrder(invalidOrder, accessToken)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}