package net.ancronik.cookbook.backend.application.exceptions;

/**
 * Exceptions which should be thrown when some entry does not exist in database.
 *
 * Should be thrown/propagated by services to controller and processed by global handler.
 *
 * @author Nikola Presecki
 */
public class DataDoesNotExistException extends Exception {

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public DataDoesNotExistException(String message) {
        super(message);
    }

}
