package net.ancronik.cookbook.backend.api.domain.service.impl;

import net.ancronik.cookbook.backend.api.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.api.domain.service.AuthenticationService;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public String getAuthenticatedUsername() throws EmptyDataException {
        return "testUser"; //FIXME mocked data for now
    }

    @Override
    public boolean isGivenUserTheRequester(@Nullable String username) throws EmptyDataException {
        return true; //FIXME mocked data
    }
}
