package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table("authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @PrimaryKey
    private Integer id;

    private String username;

    private String fullName;

    private String country;

    private LocalDate dateOfBirth;

    private String bio;

    private String image;

}
