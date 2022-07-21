package net.ancronik.cookbook.backend.api.application.exceptions;

public class EmptyDataException extends RuntimeException {

    public EmptyDataException(String message) {
        super(message);
    }

}
