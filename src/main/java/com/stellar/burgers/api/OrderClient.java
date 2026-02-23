package com.stellar.burgers.api;

import com.stellar.burgers.Constants;
import com.stellar.burgers.api.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Создание заказа с авторизацией")
    public Response createOrder(Order order, String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(Constants.CREATE_ORDER_ENDPOINT);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(Constants.INGREDIENTS_ENDPOINT);
    }
}