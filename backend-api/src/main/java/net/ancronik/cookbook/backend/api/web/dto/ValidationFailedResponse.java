package net.ancronik.cookbook.backend.api.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response for failed validation.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ValidationFailedResponse extends ApiErrorResponse {

    private Map<String, Object> errors;

    public ValidationFailedResponse(String error, String description, LocalDateTime timestamp, Map<String, Object> invalidFields) {
        super(error, description, timestamp);
        this.errors = invalidFields;
    }

}
