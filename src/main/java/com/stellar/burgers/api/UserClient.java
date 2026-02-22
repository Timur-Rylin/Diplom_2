package com.stellar.burgers.api;

import com.stellar.burgers.Constants;
import com.stellar.burgers.api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT);
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Constants.LOGIN_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .when()
                .delete(Constants.USER_ENDPOINT);
    }
}