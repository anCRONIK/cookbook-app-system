package net.ancronik.cookbook.backend.web.dto.author;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "authors", itemRelation = "author")
@JsonRootName(value = "author")
public class AuthorDto extends RepresentationModel<AuthorDto> {

    private String username;

    private String fullName;

    private LocalDate dateOfBirth;

    private String bio;

    private String imageUrl;

}
