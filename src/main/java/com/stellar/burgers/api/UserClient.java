package com.stellar.burgers.api;

import com.stellar.burgers.Constants;
import com.stellar.burgers.api.models.User;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT);
    }

    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(Constants.LOGIN_ENDPOINT);
    }

    public Response deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .when()
                .delete(Constants.USER_ENDPOINT);
    }
}