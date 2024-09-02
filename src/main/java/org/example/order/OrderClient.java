package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.client.Client;

public class OrderClient extends Client {
    public static final String ROOT_ORDERS = "/orders";
    public static final String ROOT_INGREDIENTS = "/ingredients";

    @Step("Создать заказ без авторизации")
    public ValidatableResponse createUnauthorized(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ROOT_ORDERS)
                .then().log().all();
    }

    @Step("Создать заказ с авторизацией")
    public ValidatableResponse createAuthorized(Order order, String accessToken) {
        return spec()
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(order)
                .when()
                .post(ROOT_ORDERS)
                .then().log().all();
    }

    @Step("Получить список всех актуальных ингредиентов")
    public ValidatableResponse getAllAvailableIngredients() {
        return spec()
                .when()
                .get(ROOT_INGREDIENTS)
                .then().log().all();
    }

    @Step("Получить список заказов пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return spec()
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .get(ROOT_ORDERS)
                .then().log().all();
    }
}