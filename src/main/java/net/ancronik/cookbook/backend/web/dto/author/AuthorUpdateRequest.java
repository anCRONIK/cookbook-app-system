package net.ancronik.cookbook.backend.web.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUpdateRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String bio;

    private String imageUrl;

}
