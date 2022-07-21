package net.ancronik.cookbook.backend.api.application.exceptions;

public class CdnException extends Exception {

    public CdnException(String message) {
        super(message);
    }

    public CdnException(Throwable throwable) {
        super(throwable);
    }
}
