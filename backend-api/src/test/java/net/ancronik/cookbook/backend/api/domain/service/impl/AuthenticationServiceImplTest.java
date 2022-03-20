package net.ancronik.cookbook.backend.api.domain.service.impl;

import net.ancronik.cookbook.backend.api.TestTypes;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(TestTypes.UNIT)
public class AuthenticationServiceImplTest {

    AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl();

    @Test
    public void getAuthenticatedUsername_ReturnMockData() {
        assertEquals("testUser", authenticationService.getAuthenticatedUsername());
    }

    @Test
    public void isGivenUserTheRequester_ReturnMockData() {
        assertTrue(authenticationService.isGivenUserTheRequester("roki"));
    }

}
