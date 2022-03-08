package net.ancronik.cookbook.backend.application.exceptions;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Exceptions which should be thrown when some entry couldn't be saved in db because data is invalid.
 * <p>
 * Should be thrown/propagated by services to controller and processed by global handler.
 *
 * @author Nikola Presecki
 */
public class IllegalDataInRequestException extends Exception {

    @Getter
    private final Map<String, String> invalidFields;

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public IllegalDataInRequestException(String message) {
        this(message, new HashMap<>());
    }

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public IllegalDataInRequestException(String message, @NonNull Map<String, String> invalidFields) {
        super(message);
        this.invalidFields = invalidFields;
    }

    /**
     * Method which will transform map of invalid fields to human-readable format which can be returned to user.
     *
     * @return map of invalid fields as string. Can be empty string if map is empty.
     */
    public String getInvalidFieldsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : invalidFields.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        return stringBuilder.toString();
    }

}
