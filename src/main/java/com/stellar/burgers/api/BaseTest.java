package com.stellar.burgers.api;

import com.stellar.burgers.api.models.User;
import com.stellar.burgers.api.models.LoginResponse;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;

import static org.apache.http.HttpStatus.SC_OK;

public class BaseTest {
    protected UserClient userClient = new UserClient();
    protected OrderClient orderClient = new OrderClient();
    protected User testUser;
    protected String accessToken;

    @Before
    @Step("Подготовка тестовых данных: создание пользователя")
    public void setUp() {
        String email = "testuser_" + System.currentTimeMillis() + "@example.com";
        testUser = new User(email, "password123", "Test User");

        LoginResponse response = userClient.createUser(testUser)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(LoginResponse.class);

        accessToken = response.getAccessToken();
    }

    @After
    @Step("Очистка тестовых данных: удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken)
                    .then()
                    .statusCode(SC_OK);
        }
    }

    @Step("Регистрация пользователя")
    protected void registerUser() {
        LoginResponse response = userClient.createUser(testUser)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(LoginResponse.class);
        accessToken = response.getAccessToken();
    }
}