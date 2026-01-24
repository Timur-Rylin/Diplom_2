package com.stellar.burgers.tests;

import com.stellar.burgers.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты входа пользователя")
public class LoginUserTest {

    @Test
    @DisplayName("Успешный вход существующего пользователя")
    @Description("Позитивный тест: вход с корректными учетными данными")
    public void loginExistingUserSuccessfully() {
        String email = "loginuser_" + System.currentTimeMillis() + "@example.com";
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
                .post(Constants.LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Негативный тест: вход с некорректным паролем")
    public void loginWithWrongPasswordShouldFail() {
        String email = "wrongpass_" + System.currentTimeMillis() + "@example.com";
        String registerBody = String.format(
                "{\"email\":\"%s\",\"password\":\"correct123\",\"name\":\"Test User\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT);

        String loginBody = String.format(
                "{\"email\":\"%s\",\"password\":\"wrong_password\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post(Constants.LOGIN_ENDPOINT)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS));
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Негативный тест: вход с некорректным email")
    public void loginWithWrongEmailShouldFail() {
        String email = "wrongemail_" + System.currentTimeMillis() + "@example.com";
        String registerBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\",\"name\":\"Test User\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post(Constants.CREATE_USER_ENDPOINT);

        String loginBody = "{\"email\":\"wrong_email@example.com\",\"password\":\"password123\"}";

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post(Constants.LOGIN_ENDPOINT)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS));
    }

    @Test
    @DisplayName("Вход несуществующего пользователя")
    @Description("Негативный тест: вход пользователя, который не был зарегистрирован")
    public void loginNonExistentUserShouldFail() {
        String email = "nonexistent_" + System.currentTimeMillis() + "@example.com";
        String loginBody = String.format(
                "{\"email\":\"%s\",\"password\":\"password123\"}",
                email
        );

        given()
                .baseUri(Constants.BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post(Constants.LOGIN_ENDPOINT)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(Constants.ERROR_INCORRECT_CREDENTIALS));
    }
}