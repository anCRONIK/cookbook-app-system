package net.ancronik.cookbook.backend.domain.service.impl;

import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.TestConfigurationForUnitTesting;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.domain.service.AuthorService;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationForUnitTesting.class)
@Tag(TestTypes.UNIT)
public class AuthorServiceImplValidationTest {

    @Autowired
    private AuthorService authorService;

    @Test
    public void getAuthor_InvalidId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(null));
        assertEquals("getAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(""));
        assertTrue(t.getMessage().startsWith("getAuthor.id:"));

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor("   \t   \r"));
        assertEquals("getAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor("a"));
        assertEquals("getAuthor.id: length must be between 2 and 12", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(StringTestUtils.getRandomStringInLowerCase(13)));
        assertEquals("getAuthor.id: length must be between 2 and 12", t.getMessage());
    }

    @Test
    public void createAuthor_InvalidId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> authorService.createAuthor(new AuthorCreateRequest()));
        assertEquals("createAuthor.request.username: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.createAuthor(new AuthorCreateRequest("")));
        assertTrue(t.getMessage().startsWith("createAuthor.request.username:"));

        t = assertThrows(ConstraintViolationException.class, () -> authorService.createAuthor(new AuthorCreateRequest("   \t   \r")));
        assertEquals("createAuthor.request.username: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.createAuthor(new AuthorCreateRequest("a")));
        assertEquals("createAuthor.request.username: length must be between 2 and 12", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.createAuthor(new AuthorCreateRequest(StringTestUtils.getRandomStringInLowerCase(13))));
        assertEquals("createAuthor.request.username: length must be between 2 and 12", t.getMessage());
    }


    @Test
    public void updateAuthor_InvalidId_ThrowValidationException() {
        AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest();
        authorUpdateRequest.setFullName("new name");

        Throwable t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor(null, authorUpdateRequest));
        assertEquals("updateAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("", authorUpdateRequest));
        assertTrue(t.getMessage().startsWith("updateAuthor.id:"));

        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("   \t   \r", authorUpdateRequest));
        assertEquals("updateAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("a", authorUpdateRequest));
        assertEquals("updateAuthor.id: length must be between 2 and 12", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor(StringTestUtils.getRandomStringInLowerCase(13), authorUpdateRequest));
        assertEquals("updateAuthor.id: length must be between 2 and 12", t.getMessage());
    }

    AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest();
    @Test
    public void updateAuthor_InvalidRequest_ThrowValidationException() {
        authorUpdateRequest.setFullName(StringTestUtils.getRandomStringInLowerCase(34));
        Throwable t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("testUser", authorUpdateRequest));
        assertEquals("updateAuthor.request.fullName: length must be between 0 and 30", t.getMessage());

        authorUpdateRequest = new AuthorUpdateRequest();
        authorUpdateRequest.setBio(StringTestUtils.getRandomStringInLowerCase(2022));
        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("testUser", authorUpdateRequest));
        assertEquals("updateAuthor.request.bio: length must be between 0 and 2000", t.getMessage());

        authorUpdateRequest = new AuthorUpdateRequest();
        authorUpdateRequest.setDateOfBirth(LocalDate.now().plusDays(1));
        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("testUser", authorUpdateRequest));
        assertEquals("updateAuthor.request.dateOfBirth: must be a past date", t.getMessage());

        authorUpdateRequest = new AuthorUpdateRequest();
        authorUpdateRequest.setImageUrl("not_url");
        t = assertThrows(ConstraintViolationException.class, () -> authorService.updateAuthor("testUser", authorUpdateRequest));
        assertEquals("updateAuthor.request.imageUrl: must be a valid URL", t.getMessage());
    }

}
