package net.ancronik.cookbook.backend.api.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    @NotNull
    @CodePointLength(min = 2, max = 100)
    private String name;

    @Size(min = 1, max = 6)
    @Pattern(regexp = "^\\d+[/.]?\\d*$")
    private String quantity;

    @Size(max = 8)
    private String measurementUnit;

}
