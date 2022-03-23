package net.ancronik.cookbook.backend.authentication.domain.mapper;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag(TestTypes.UNIT)
public class BasicUserToUserDetailsMapperTest {

    private final BasicUserToUserDetailsMapper mapper = new BasicUserToUserDetailsMapper();

    @Test
    public void map_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(null));
    }

    @Test
    public void map_BasicUserWithEmptyUsernameGiven_ExceptionThrownByUserDetails() {
        User user = new User();
        user.setEmail("test-user@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> mapper.map(user));
    }

    @Test
    public void map_BasicUserGiven_ExceptionThrownByMethodForMappingAuthorities() {
        BasicUser basicUser = new BasicUser();
        basicUser.setUsername("testUser");
        basicUser.setEmail("test-user@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> mapper.map(basicUser));
    }

    @Test
    public void map_UserGiven_ReturnValidUserDetails() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test-user@gmail.com");

        UserDetails userDetails = mapper.map(user);

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertNull(user.getPasswordHash());
        assertEquals(user.isAccountLocked(), !userDetails.isAccountNonLocked());
        assertEquals(user.isAccountDisabled(), !userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertEquals(1, userDetails.getAuthorities().size());

        user.setAccountDisabled(true);

        userDetails = mapper.map(user);

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.isAccountLocked(), !userDetails.isAccountNonLocked());
        assertEquals(user.isAccountDisabled(), !userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void map_AdminGiven_ReturnValidUserDetails() {
        Admin admin = new Admin();
        admin.setUsername("testAdmin");
        admin.setEmail("test-admin@gmail.com");

        UserDetails userDetails = mapper.map(admin);

        assertEquals(admin.getUsername(), userDetails.getUsername());
        assertNull(admin.getPasswordHash());
        assertEquals(admin.isAccountLocked(), !userDetails.isAccountNonLocked());
        assertEquals(admin.isAccountDisabled(), !userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertEquals(1, userDetails.getAuthorities().size());

        admin.setAccountDisabled(true);
        admin.setPasswordResetRequired(true);

        userDetails = mapper.map(admin);

        assertEquals(admin.getUsername(), userDetails.getUsername());
        assertEquals(admin.isAccountLocked(), !userDetails.isAccountNonLocked());
        assertEquals(admin.isAccountDisabled(), !userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void map_ListGiven_ReturnValidUserDetails() {
        Admin admin = new Admin();
        admin.setUsername("testAdmin");
        admin.setEmail("test-admin@gmail.com");

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test-user@gmail.com");

        List<UserDetails> userDetails = mapper.mapList(List.of(admin, user));


        assertEquals(2, userDetails.size());
    }

}
