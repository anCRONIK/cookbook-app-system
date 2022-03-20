package net.ancronik.cookbook.backend.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response which is returned in case of some API exception which wasn't handled by domain or data layer.
 *
 * @author Nikola Presecki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    protected String error;

    protected String description;

    protected LocalDateTime timestamp;

}
