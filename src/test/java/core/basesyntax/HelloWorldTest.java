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

    // Константи для логінів
    private static final String SHORT_LOGIN_3 = "abc";
    private static final String SHORT_LOGIN_5 = "abcde";
    private static final String EDGE_LOGIN_6 = "abcdef";
    private static final String LONG_LOGIN_8 = "abcdefgh";

    // Константи для паролів
    private static final String EMPTY_PASSWORD = "";
    private static final String SHORT_PASSWORD_3 = "123";
    private static final String SHORT_PASSWORD_5 = "12345";
    private static final String EDGE_PASSWORD_6 = "123456";
    private static final String LONG_PASSWORD_8 = "12345678";

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
        User user = createValidUser();
        User registeredUser = registrationService.register(user);

        assertEquals(user, registeredUser);
        assertNotNull(storageDao.get(VALID_LOGIN), "User should be added to Storage");
    }

    @Test
    void register_loginTooShort_notOk() {
        User user = createValidUser();

        user.setLogin(EMPTY_PASSWORD); // 0
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setLogin(SHORT_LOGIN_3); // 3
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setLogin(SHORT_LOGIN_5); // 5
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_loginLength6_ok() {
        User user = createValidUser();
        user.setLogin(EDGE_LOGIN_6);
        User registeredUser = registrationService.register(user);
        assertNotNull(storageDao.get(EDGE_LOGIN_6));
    }

    @Test
    void register_passwordBoundaries_notOk() {
        User user = createValidUser();

        user.setPassword(EMPTY_PASSWORD);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setPassword(SHORT_PASSWORD_3);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

        user.setPassword(SHORT_PASSWORD_5);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_passwordLength6_ok() {
        User user = createValidUser();
        user.setPassword(EDGE_PASSWORD_6);
        registrationService.register(user);
        assertNotNull(storageDao.get(VALID_LOGIN));
    }

    @Test
    void register_passwordLength8_ok() {
        User user = createValidUser();
        user.setPassword(LONG_PASSWORD_8);
        registrationService.register(user);
        assertNotNull(storageDao.get(VALID_LOGIN));
    }

    @Test
    void register_ageExactly18_ok() {
        User user = createValidUser();
        user.setAge(VALID_AGE);
        registrationService.register(user);
        assertNotNull(storageDao.get(VALID_LOGIN));
    }

    @Test
    void register_negativeAge_notOk() {
        User user = createValidUser();
        user.setAge(-1);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_notOk() {
        User user = createValidUser();
        user.setAge(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createValidUser();
        user.setLogin(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createValidUser();
        user.setPassword(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_duplicateLogin_notOk() {
        User existingUser = new User();
        existingUser.setLogin(VALID_LOGIN);
        Storage.people.add(existingUser);

        User newUser = createValidUser();
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }

    private User createValidUser() {
        User user = new User();
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(VALID_AGE);
        return user;
    }
}
