package net.ancronik.cookbook.backend.api.application;

/**
 * All constants that application needs.
 *
 * @author Nikola Presecki
 */
public abstract class Constants {

    /**
     * Name of HTTP header that will contain request id
     */
    public static final String HEADER_CLID = "X-CLID";

    /**
     * Key in logging context for CLID.
     */
    public static final String LOGGING_CLID = "clid";

}
