package user;

import io.qameta.allure.junit4.DisplayName;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    private User user;
    private final UserGenerator userGenerator = new UserGenerator();
    private String userAccessToken;
    private final UserClient userClient = new UserClient();

    @Before
    public void setUp() {
        user = userGenerator.random();
    }

    @After
    public void deleteUser() {
        userAccessToken = userClient.getAccessTokenOnLogin(user);
        System.out.println("Удаляем пользователя с userAccessToken, если он не null: " + userAccessToken);
        if (userAccessToken != null) {
            userClient.delete(userAccessToken);
        }
    }

    @Test
    @DisplayName("Успешный логин под существующим пользователем.")
    public void loginWithExistingUser(){
        userClient.create(user);
        userClient.login(user).assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Не проходит логин пользователя с неверным логином и паролем.")
    public void loginWithWrongLoginAndPasswordFails(){
        userClient.create(user);
        User corruptedUser = userGenerator.random();
        userClient.login(corruptedUser).assertThat().statusCode(401).and().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));
    }
}
