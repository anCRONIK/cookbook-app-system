package net.ancronik.cookbook.backend.web.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateRequest {

    private String username;

    private LocalDate dateOfBirth;

}
