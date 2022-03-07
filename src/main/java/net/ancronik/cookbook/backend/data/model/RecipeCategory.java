package net.ancronik.cookbook.backend.data.model;

import lombok.Getter;

/**
 * Enum representing recipe category.
 *
 * @author Nikola Presecki
 */
@Getter
public enum RecipeCategory {

    APPETIZER("APPETIZER"), ENTREE("ENTREE"), DESSERT("DESSERT");

    private final String category;

    RecipeCategory(String category) {
        this.category = category;
    }

}
