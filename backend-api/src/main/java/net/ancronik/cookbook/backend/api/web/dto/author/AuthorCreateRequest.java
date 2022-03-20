package net.ancronik.cookbook.backend.api.web.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateRequest {

    @NotBlank
    @CodePointLength(min = 2, max = 12)
    private String username;

}
