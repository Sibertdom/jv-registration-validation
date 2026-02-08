package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        Storage.people.clear(); // Очищуємо базу перед кожним тестом
    }

    @Test
    void register_validUser_Ok() {
        User user = new User();
        user.setLogin("validLogin");
        user.setPassword("validPassword");
        user.setAge(20);
        User registeredUser = registrationService.register(user);
        assertEquals(user, registeredUser);
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User();
        user.setLogin("short");
        user.setPassword("password123");
        user.setAge(20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User();
        user.setLogin("longEnoughLogin");
        user.setPassword("12345");
        user.setAge(20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_underageUser_notOk() {
        User user = new User();
        user.setLogin("validLogin");
        user.setPassword("password123");
        user.setAge(17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_duplicateLogin_notOk() {
        User user1 = new User();
        user1.setLogin("uniqueLogin");
        user1.setPassword("password123");
        user1.setAge(25);
        registrationService.register(user1);

        User user2 = new User();
        user2.setLogin("uniqueLogin"); // такий самий логін
        user2.setPassword("anotherPassword");
        user2.setAge(30);
        assertThrows(RegistrationException.class, () -> registrationService.register(user2));
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User();
        user.setLogin("validLogin");
        user.setPassword("password123");
        user.setAge(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }
}
