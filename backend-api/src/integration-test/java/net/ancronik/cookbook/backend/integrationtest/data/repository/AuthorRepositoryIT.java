package net.ancronik.cookbook.backend.integrationtest.data.repository;

import net.ancronik.cookbook.backend.api.CookbookBackendApiSpringBootApp;
import net.ancronik.cookbook.backend.integrationtest.CassandraTestContainersExtension;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.data.model.AuthorMockData;
import net.ancronik.cookbook.backend.api.data.repository.AuthorRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = CookbookBackendApiSpringBootApp.class)
@ExtendWith({SpringExtension.class, CassandraTestContainersExtension.class})
@Tag(TestTypes.INTEGRATION)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryIT {

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
