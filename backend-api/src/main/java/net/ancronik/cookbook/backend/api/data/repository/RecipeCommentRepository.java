package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentPK;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCommentRepository extends CassandraRepository<RecipeComment, RecipeCommentPK> {

    Slice<RecipeComment> findAllByRecipeCommentPKRecipeId(Long recipeId, Pageable pageable);

}
