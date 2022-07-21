package net.ancronik.cookbook.backend.authentication.application.exception;

/**
 * Exception which should be thrown in case of some unexpected exception.
 * <p>
 * This is just wrapper for better exception handling.
 *
 * @author Nikola Presecki
 */
public class InternalException extends RuntimeException {

    /**
     * Constructor with one argument.
     *
     * @param t throwable
     */
    public InternalException(Throwable t) {
        super(t);
    }

}
