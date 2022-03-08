package net.ancronik.cookbook.backend.domain.service;

/**
 * Service that handles all things regarding in app authentication.
 *
 * @author Nikola Presecki
 */
public interface AuthenticationService {

    /**
     * Method for getting username of the current authenticated user.
     *
     * @return username of the authenticated user
     */
    String getAuthenticatedUsername();

}
