package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.Recipe;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

/**
 * Db repository for {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface RecipeRepository extends CassandraRepository<Recipe, Long> {

    Slice<Recipe> findAllByCategory(String category, Pageable pageable);

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("not supported for this repository");
    }
}
