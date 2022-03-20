package net.ancronik.cookbook.backend.api.web.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUpdateRequest {

    @CodePointLength(max = 30)
    private String fullName;

    @Past
    private LocalDate dateOfBirth;

    @CodePointLength(max = 2000)
    private String bio;

    @URL
    private String imageUrl;

}
