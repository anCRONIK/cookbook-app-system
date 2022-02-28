package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.data.model.Author;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CassandraRepository<Author, String> {
}
