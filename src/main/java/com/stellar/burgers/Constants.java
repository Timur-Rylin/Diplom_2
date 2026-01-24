package com.stellar.burgers;

public class Constants {
    public static final String BASE_URL = "https://stellarburgers.education-services.ru";

    public static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String CREATE_ORDER_ENDPOINT = "/api/orders";

    public static final String ERROR_USER_EXISTS = "User already exists";
    public static final String ERROR_REQUIRED_FIELDS = "Email, password and name are required fields";
    public static final String ERROR_INCORRECT_CREDENTIALS = "email or password are incorrect";
    public static final String ERROR_NOT_AUTHORIZED = "You should be authorised";
    public static final String ERROR_NO_INGREDIENTS = "Ingredient ids must be provided";

    public static final String[] VALID_INGREDIENTS = {
            "6043b41abdacab0626a733c6",
            "609646e4dc916e00276b2870"
    };

    public static final String INVALID_INGREDIENT_HASH = "invalid_hash_12345";
}