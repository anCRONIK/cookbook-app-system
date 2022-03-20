package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.data.model.Author;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Db repository for {@link Author}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface AuthorRepository extends CassandraRepository<Author, String> {

    @Override
    default void deleteById(String s) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void delete(Author entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAllById(Iterable<? extends String> strings) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll(Iterable<? extends Author> entities) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("not supported for this repository");
    }

}
