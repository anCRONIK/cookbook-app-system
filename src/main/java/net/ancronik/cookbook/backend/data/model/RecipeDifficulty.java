package net.ancronik.cookbook.backend.data.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Enum for recipe difficulty rating.
 *
 * @author Nikola Presecki
 */
@Getter
@ToString
public enum RecipeDifficulty {
    LOW(1), MEDIUM(2), HARD(3), HARDEST(4), EXTREME(5);


    private final int difficulty;

    RecipeDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public static RecipeDifficulty parse(Integer difficulty) {
        return RecipeDifficulty.values()[difficulty - 1];
    }

}
