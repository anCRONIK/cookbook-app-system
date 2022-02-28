package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table("authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    //TODO limits
    @PrimaryKey
    private String username;
    //TODO limits
    @Column(value = "full_name")
    private String fullName;
    //TODO limits
    @Column(value = "date_of_birth")
    private LocalDate dateOfBirth;
    //TODO limits
    @Column(value = "bio")
    private String bio;
    //TODO limits
    @Column(value = "image_url")
    private String imageUrl;

}
