package order;

import io.qameta.allure.junit4.DisplayName;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderGenerator;
import org.junit.After;
import org.junit.Test;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;

import static org.hamcrest.Matchers.equalTo;

public class UserOrdersTest {

    private final UserGenerator userGenerator = new UserGenerator();
    private final OrderGenerator generator = new OrderGenerator();
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String userAccessToken;

    @After
    public void deleteUser() {
        if (userAccessToken != null) {
            userClient.delete(userAccessToken);
        }
    }

    @Test
    @DisplayName("Получение заказа авторизованного пользователя.")
    public void getOrdersAuthorizedUser() {
        Order order = generator.simple();
        User user = userGenerator.random();
        userAccessToken = userClient.create(user).extract().body().path("accessToken");
        //Получаю order_id созданного заказа, чтобы после сравнить, что именно он вернется в запросе заказов пользователя.
        String createdOrderId = orderClient.createAuthorized(order, userAccessToken).extract().body().path("order._id");
        orderClient.getUserOrders(userAccessToken).assertThat().statusCode(200).and().body("success", equalTo(true)).body("orders[0]._id", equalTo(createdOrderId));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя.")
    public void getOrdersUnauthorizedUser() {
        orderClient.getUserOrders("").assertThat().statusCode(401).and().body("success", equalTo(false)).body("message", equalTo("You should be authorised"));
    }
}