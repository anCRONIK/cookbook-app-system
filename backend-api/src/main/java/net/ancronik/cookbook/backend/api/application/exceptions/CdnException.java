package net.ancronik.cookbook.backend.api.application.exceptions;

/**
 * Exception which should be thrown in case of some CDN exception
 * <p>
 * Should be thrown/propagated by services to controller and processed by global handler.
 *
 * @author Nikola Presecki
 */
public class CdnException extends Exception {

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public CdnException(String message) {
        super(message);
    }

    /**
     * Constructor with throwable.
     *
     * @param throwable throwable
     */
    public CdnException(Throwable throwable) {
        super(throwable);
    }
}
