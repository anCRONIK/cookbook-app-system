package net.ancronik.cookbook.backend.api.domain.service;

import net.ancronik.cookbook.backend.api.application.exceptions.EmptyDataException;

import javax.annotation.Nullable;

/**
 * Service that handles all things regarding in app authentication.
 *
 * @author Nikola Presecki
 */
public interface AuthenticationService {

    /**
     * Method for getting username of the current authenticated user.
     * <p>
     * Never returns null or empty string, if username can not be accessed method will throw exception.
     *
     * @return username of the authenticated user
     * @throws EmptyDataException in case that username is null or empty
     */
    String getAuthenticatedUsername() throws EmptyDataException;

    /**
     * Method checks if given username corresponds to the authenticated user which made the request.
     *
     * @param username username to check, can be even null
     * @return {@literal true} if username corresponds, otherwise false
     * @throws EmptyDataException in case of error while checking authenticated user
     */
    boolean isGivenUserTheRequester(@Nullable String username) throws EmptyDataException;
}
