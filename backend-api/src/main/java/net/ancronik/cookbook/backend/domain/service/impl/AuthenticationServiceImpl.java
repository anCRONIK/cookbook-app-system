package net.ancronik.cookbook.backend.domain.service.impl;

import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.domain.service.AuthenticationService;

/**
 * Implementation of {@link AuthenticationService}.
 *
 * @author Nikola Presecki
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public String getAuthenticatedUsername() throws EmptyDataException {
        return "testUser"; //FIXME mocked data for now
    }
}
