package net.ancronik.cookbook.backend.api.application.exceptions;

public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String message) {
        super(message);
    }

}
