package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.RecipeComment;
import net.ancronik.cookbook.backend.data.model.RecipeCommentPK;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link RecipeComment}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface RecipeCommentRepository extends CassandraRepository<RecipeComment, RecipeCommentPK> {

    Slice<RecipeComment> findAllByRecipeId(Long recipeId, Pageable pageable);

}
