package net.ancronik.cookbook.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Response which is returned in case of some API exception which wasn't handled by domain or data layer.
 *
 * @author Nikola Presecki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private String error;

    private String description;

    private ZonedDateTime timestamp;

}
