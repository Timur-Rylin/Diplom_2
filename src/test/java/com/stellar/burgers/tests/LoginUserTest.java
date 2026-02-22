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

@DisplayName("Тесты входа пользователя")
public class LoginUserTest extends BaseTest {

    @Test
    @DisplayName("Успешный вход существующего пользователя")
    @Description("Позитивный тест: вход с корректными учетными данными")
    public void loginExistingUserSuccessfully() {
        LoginResponse response = userClient.loginUser(testUser)
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
        accessToken = response.getAccessToken();
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Негативный тест: вход с некорректным паролем")
    public void loginWithWrongPasswordShouldFail() {
        User userWithWrongPassword = new User(testUser.getEmail(), "wrong_password", testUser.getName());

        ApiResponse response = userClient.loginUser(userWithWrongPassword)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_INCORRECT_CREDENTIALS, response.getMessage());
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Негативный тест: вход с некорректным email")
    public void loginWithWrongEmailShouldFail() {
        User userWithWrongEmail = new User("wrong_email@example.com", testUser.getPassword(), testUser.getName());

        ApiResponse response = userClient.loginUser(userWithWrongEmail)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_INCORRECT_CREDENTIALS, response.getMessage());
    }

    @Test
    @DisplayName("Вход несуществующего пользователя")
    @Description("Негативный тест: вход пользователя, который не был зарегистрирован")
    public void loginNonExistentUserShouldFail() {
        User nonExistentUser = new User("nonexistent@example.com", "password123", "Non Existent");

        ApiResponse response = userClient.loginUser(nonExistentUser)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS))
                .extract()
                .as(ApiResponse.class);

        assertEquals("Error message should match", Constants.ERROR_INCORRECT_CREDENTIALS, response.getMessage());
    }
}