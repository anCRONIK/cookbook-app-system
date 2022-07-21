package net.ancronik.cookbook.backend.api.domain.service.impl;

import net.ancronik.cookbook.backend.api.TestTypes;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(TestTypes.UNIT)
class AuthenticationServiceImplTest {

    AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl();

    @Test
    void getAuthenticatedUsername_ReturnMockData() {
        assertEquals("testUser", authenticationService.getAuthenticatedUsername());
    }

    @Test
    void isGivenUserTheRequester_ReturnMockData() {
        assertTrue(authenticationService.isGivenUserTheRequester("roki"));
    }

}
