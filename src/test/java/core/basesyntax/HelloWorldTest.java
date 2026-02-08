package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
    private static final String VALID_LOGIN = "validUser";
    private static final String VALID_PASSWORD = "password123";
    private static final int VALID_AGE = 18;

    private RegistrationService registrationService;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        storageDao = new StorageDaoImpl();
        Storage.people.clear();
    }

    @Test
    void register_validUser_Ok() {
        User user = new User();
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(VALID_AGE);

        User registeredUser = registrationService.register(user);

        assertEquals(user, registeredUser);
        assertNotNull(storageDao.get(VALID_LOGIN), "User should be added to Storage");
    }

    @Test
    void register_duplicateLogin_notOk() {
        User existingUser = new User();
        existingUser.setLogin(VALID_LOGIN);
        Storage.people.add(existingUser); // Додаємо напряму в Storage

        User newUser = new User();
        newUser.setLogin(VALID_LOGIN);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setAge(20);

        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }

    @Test
    void register_passwordBoundaries_notOk() {
        User user = new User();
        user.setLogin(VALID_LOGIN);
        user.setAge(VALID_AGE);

        user.setPassword(""); // 0 characters
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setPassword("abc"); // 3 characters
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setPassword("abcde"); // 5 characters
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageBoundaries_notOk() {
        User user = new User();
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);

        user.setAge(-1); // negative
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setAge(17); // under 18
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullValues_notOk() {
        User user = new User();

        user.setLogin(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setLogin(VALID_LOGIN);
        user.setPassword(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }
}