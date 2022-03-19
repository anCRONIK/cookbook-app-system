package net.ancronik.cookbook.backend.data.repository;

import net.ancronik.cookbook.backend.DatabaseIntegrationTest;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.data.model.AuthorMockData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryIT extends DatabaseIntegrationTest {

    @Autowired
    AuthorRepository authorRepository;


    @Order(1)
    @Test
    public void createAuthorMultipleAuthors() {
        authorRepository.saveAll(AuthorMockData.generateRandomMockData(10));
    }

    @Order(2)
    @Test
    public void readAllSavedAuthorsInDatabase_TestPageable() {
        int counter = 0;
        Slice<Author> authors = authorRepository.findAll(Pageable.ofSize(2));
        counter += authors.getNumberOfElements();

        do {
            authors = authorRepository.findAll(authors.nextPageable());
            counter += authors.getNumberOfElements();
        } while (authors.hasNext());

        assertEquals(10, counter);
    }

    @Order(3)
    @Test
    public void saveAuthorWithSameUsernameTwice() {
        Author author = new Author();
        author.setUsername("testForDuplicate");
        authorRepository.save(author);

        Author author1 = new Author();
        author1.setUsername("testForDuplicate");
        author1.setFullName("test fullname");
        authorRepository.save(author1);

        List<Author> duplicate = authorRepository.findAllById(List.of("testForDuplicate"));
        assertEquals(1, duplicate.size());
        assertEquals("test fullname", duplicate.get(0).getFullName());
    }

    @Order(4)
    @Test
    public void readAllSavedAuthorsInDatabase_CheckForDuplicate() {
        assertEquals(11, authorRepository.count());
    }

    @Order(5)
    @Test
    public void saveEmptyAuthor_ThrowExceptionBecausePrimaryKey() {
        final Author author = new Author();
        assertThrows(DataAccessException.class, () -> authorRepository.save(author));
        author.setUsername("");
        assertThrows(DataAccessException.class, () -> authorRepository.save(author));
    }

    @Order(6)
    @Test
    public void testThatDeleteOperationsAreNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> authorRepository.delete(AuthorMockData.generateRandomMockData(1).get(0)));
        assertThrows(UnsupportedOperationException.class, () -> authorRepository.deleteAll(AuthorMockData.generateRandomMockData(3)));
        assertThrows(UnsupportedOperationException.class, () -> authorRepository.deleteAll());
        assertThrows(UnsupportedOperationException.class, () -> authorRepository.deleteAllById(List.of("id")));
    }

}
