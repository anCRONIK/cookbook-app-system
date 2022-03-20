package net.ancronik.cookbook.backend.api.application.exceptions;

/**
 * Exception that should be used in case were empty data would be returned to the user and that is invalid
 * for that use case.
 *
 * @author Nikola Presecki
 */
public class EmptyDataException extends RuntimeException {

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public EmptyDataException(String message) {
        super(message);
    }

}
