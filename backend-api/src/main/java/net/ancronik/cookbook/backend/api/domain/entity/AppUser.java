package net.ancronik.cookbook.backend.api.domain.entity;

import lombok.Data;

@Data
public class AppUser {

    private Integer id;

    private String username;

    private String email;

}
