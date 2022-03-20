package net.ancronik.cookbook.backend.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for uploaded image.
 *
 * @author Nikola Presecki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageResponse {

    String image;

    String thumbnail;

}
