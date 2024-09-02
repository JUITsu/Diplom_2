package org.example.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public User generic() {
        return new User("qaqa@qa.ru", "qwerty123", "tester");
    }

    public User random() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@ru.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10));
    }
}
