package net.ancronik.cookbook.backend.authentication.application.security;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(TestTypes.UNIT)
public class SecurityAuthoritiesTest {

    @Test
    public void getRolesForUser_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SecurityAuthorities.getRolesForUser(null));
    }

    @Test
    public void getRolesForUser_BasicUserGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SecurityAuthorities.getRolesForUser(new BasicUser()));
    }

    @Test
    public void getRolesForUser_UserGiven_ReturnValidRoles() {
        User user = new User();

        List<String> roles = SecurityAuthorities.getRolesForUser(user);

        assertEquals(1, roles.size());
        assertEquals(SecurityAuthorities.USER_ROLE, roles.get(0));

        user.setEditor(true);
        roles = SecurityAuthorities.getRolesForUser(user);

        assertEquals(2, roles.size());
        assertEquals(SecurityAuthorities.USER_ROLE, roles.get(0));
        assertEquals(SecurityAuthorities.EDITOR_ROLE, roles.get(1));
    }

    @Test
    public void getRolesForUser_AdminGiven_ReturnValidRoles() {
        Admin admin = new Admin();

        List<String> roles = SecurityAuthorities.getRolesForUser(admin);

        assertEquals(1, roles.size());
        assertEquals(SecurityAuthorities.ADMIN_ROLE, roles.get(0));
    }
}
