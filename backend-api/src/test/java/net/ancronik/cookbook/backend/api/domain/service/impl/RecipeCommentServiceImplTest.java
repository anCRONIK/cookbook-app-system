package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.StringTestUtils;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentPK;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCommentRepository;
import net.ancronik.cookbook.backend.api.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.api.domain.assembler.RecipeCommentModelAssembler;
import net.ancronik.cookbook.backend.api.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.api.web.dto.recipe.AddRecipeCommentRequest;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCommentModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag(TestTypes.UNIT)
public class RecipeCommentServiceImplTest {

    private final RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentModel> RecipeCommentModelAssembler = new RecipeCommentModelAssembler(new ModelMapper());

    private final AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);

    private final RecipeCommentRepository mockRecipeCommentRepository = Mockito.mock(RecipeCommentRepository.class);

    private final RecipeRepository mockRecipeRepository = Mockito.mock(RecipeRepository.class);

    private final RecipeCommentServiceImpl recipeCommentService = new RecipeCommentServiceImpl(
        RecipeCommentModelAssembler,
        mockRecipeCommentRepository,
        mockRecipeRepository,
        mockAuthenticationService
    );

    @Test
    @SneakyThrows
    void getCommentsForRecipe_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> recipeCommentService.getCommentsForRecipe(null, Pageable.unpaged()));

        verifyNoInteractions(mockRecipeCommentRepository);
        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    @SneakyThrows
    void getCommentsForRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeCommentRepository.findAllByRecipeCommentPKRecipeId(any(), any())).thenThrow(new ConcurrencyFailureException("bla"));

        assertThrows(DataAccessException.class, () -> recipeCommentService.getCommentsForRecipe(1L, Pageable.ofSize(12)));

        verify(mockRecipeCommentRepository).findAllByRecipeCommentPKRecipeId(any(), any());
        verifyNoMoreInteractions(mockRecipeCommentRepository);
        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }


    @Test
    @SneakyThrows
    void getCommentsForRecipe_RepositoryReturnsData_ReturnProperData() {
        Long id = 22L;
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        when(mockRecipeRepository.existsById(id)).thenReturn(true);
        when(mockRecipeCommentRepository.findAllByRecipeCommentPKRecipeId(id, pageable)).thenReturn(new SliceImpl<>(
            RecipeCommentMockData.generateRandomMockData(10),
            pageable,
            true
        ));

        Slice<RecipeCommentModel> data = recipeCommentService.getCommentsForRecipe(id, pageable);

        assertNotNull(data);
        assertEquals(10, data.getNumberOfElements());

        verify(mockRecipeCommentRepository).findAllByRecipeCommentPKRecipeId(id, pageable);
        verifyNoMoreInteractions(mockRecipeCommentRepository);
        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }


    @Test
    @SneakyThrows
    void addCommentToRecipe_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> recipeCommentService.addCommentToRecipe(null, new AddRecipeCommentRequest("text")));
        assertThrows(IllegalArgumentException.class, () -> recipeCommentService.addCommentToRecipe(1L, null));

        verifyNoInteractions(mockRecipeCommentRepository);
        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    @SneakyThrows
    void addCommentToRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.existsById(1L)).thenThrow(new ConcurrencyFailureException("bla"));

        assertThrows(DataAccessException.class, () -> recipeCommentService.addCommentToRecipe(1L, new AddRecipeCommentRequest("text")));

        verifyNoInteractions(mockRecipeCommentRepository);
        verify(mockRecipeRepository).existsById(1L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }


    @Test
    @SneakyThrows
    void addCommentToRecipe_RecipeWithGivenIdDoesNotExist_ThrowException() {
        when(mockRecipeRepository.existsById(1L)).thenReturn(false);

        assertThrows(DataDoesNotExistException.class, () -> recipeCommentService.addCommentToRecipe(1L, new AddRecipeCommentRequest("text")));

        verifyNoInteractions(mockRecipeCommentRepository);
        verify(mockRecipeRepository).existsById(1L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    @SneakyThrows
    void addCommentToRecipe_RepositoryReturnsData_ReturnProperData() {
        ArgumentCaptor<RecipeComment> recipeCommentCaptor = ArgumentCaptor.forClass(RecipeComment.class);
        Long id = 22L;
        String text = "Awesome text";
        AddRecipeCommentRequest request = new AddRecipeCommentRequest(text);
        String user = "user1";
        LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("UTC"));
        RecipeComment returnedRecipeComment = new RecipeComment(new RecipeCommentPK(id, user, timestamp), text);

        when(mockRecipeRepository.existsById(id)).thenReturn(true);
        when(mockRecipeCommentRepository.save(any())).thenReturn(returnedRecipeComment);
        when(mockAuthenticationService.getAuthenticatedUsername()).thenReturn(user);

        recipeCommentService.addCommentToRecipe(id, request);


        verify(mockRecipeCommentRepository).save(recipeCommentCaptor.capture());
        verifyNoMoreInteractions(mockRecipeCommentRepository);
        RecipeComment entity = recipeCommentCaptor.getValue();
        assertEquals(user, entity.getRecipeCommentPK().getUsername());
        assertEquals(id, entity.getRecipeCommentPK().getRecipeId());
        assertEquals(text, entity.getText());
        assertNotNull(entity.getRecipeCommentPK().getDateCreated());

        verify(mockRecipeRepository).existsById(id);
        verifyNoMoreInteractions(mockRecipeRepository);

        verify(mockAuthenticationService).getAuthenticatedUsername();
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @Test
    void getCommentsForRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(-1L, Pageable.ofSize(10)));
        assertEquals("getCommentsForRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }

    @Test
    void getCommentsForRecipe_InvalidPageableGiven_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(2L, Pageable.unpaged()));
        assertEquals("getCommentsForRecipe.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(2L, Pageable.ofSize(110)));
        assertEquals("getCommentsForRecipe.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());
    }

    @Test
    void addCommentToRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(-231L, new AddRecipeCommentRequest("valid text")));
        assertEquals("addCommentToRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }

    @Test
    void addCommentToRecipe_InvalidComment_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest()));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("        ")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("   \n \t  ")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(
            ConstraintViolationException.class,
            () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(21312321)))
        );
        assertEquals("addCommentToRecipe.comment.text: length must be between 0 and 10000", t.getMessage());
    }
}
