package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.Recipe;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Db repository for {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface RecipeRepository extends CassandraRepository<Recipe, Integer> {
}
