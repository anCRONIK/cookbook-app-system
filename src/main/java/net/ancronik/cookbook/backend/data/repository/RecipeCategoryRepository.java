package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Db repository for {@link RecipeCategory}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface RecipeCategoryRepository extends CassandraRepository<RecipeCategory, String> {

    @Override
    default <S extends RecipeCategory> List<S> saveAll(Iterable<S> entites) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends RecipeCategory> S insert(S entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends RecipeCategory> List<S> insert(Iterable<S> entities) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends RecipeCategory> S save(S entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteById(String s) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void delete(RecipeCategory entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAllById(Iterable<? extends String> strings) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll(Iterable<? extends RecipeCategory> entities) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("not supported for this repository");
    }
}
