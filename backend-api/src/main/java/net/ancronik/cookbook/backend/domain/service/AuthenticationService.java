package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;

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

}
