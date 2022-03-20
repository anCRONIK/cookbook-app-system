package net.ancronik.cookbook.backend.api.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;

@Table("authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    @NotBlank
    @CodePointLength(min = 2, max = 12)
    @PrimaryKey
    private String username;

    @CodePointLength(max = 30)
    @Column(value = "full_name")
    private String fullName;

    @Past
    @Column(value = "date_of_birth")
    private LocalDate dateOfBirth;

    @CodePointLength(max = 2000)
    @Column(value = "bio")
    private String bio;

    @URL
    @Column(value = "image_url")
    private String imageUrl;

}
