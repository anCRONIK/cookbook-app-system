package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.Author;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Db repository for {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface AuthorRepository extends CassandraRepository<Author, String> {
}
