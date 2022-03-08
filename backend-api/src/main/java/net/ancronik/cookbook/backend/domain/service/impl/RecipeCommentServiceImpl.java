package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.data.model.RecipeComment;
import net.ancronik.cookbook.backend.data.model.RecipeCommentPK;
import net.ancronik.cookbook.backend.data.repository.RecipeCommentRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.web.dto.recipe.AddRecipeCommentRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RecipeCommentService}.
 *
 * @author Nikola Presecki
 */
@Service
@Slf4j
public class RecipeCommentServiceImpl implements RecipeCommentService {

    private final RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentDto> recipeCommentDtoAssembler;

    private final RecipeCommentRepository recipeCommentRepository;

    private final RecipeRepository recipeRepository;

    private final AuthenticationService authenticationService;

    @Autowired
    public RecipeCommentServiceImpl(RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentDto> recipeCommentDtoAssembler, RecipeCommentRepository recipeCommentRepository, RecipeRepository recipeRepository, AuthenticationService authenticationService) {
        this.recipeCommentDtoAssembler = recipeCommentDtoAssembler;
        this.recipeCommentRepository = recipeCommentRepository;
        this.recipeRepository = recipeRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Slice<RecipeCommentDto> getCommentsForRecipe(@NonNull Long id, @NonNull Pageable pageable) throws DataDoesNotExistException {
        checkIfRecipeExists(id);

        Slice<RecipeComment> data = recipeCommentRepository.findAllByRecipeId(id, pageable);

        List<RecipeCommentDto> dtoList = data.getContent().stream().map(recipeCommentDtoAssembler::toModel).collect(Collectors.toList());

        return new SliceImpl<>(dtoList, data.getPageable(), data.hasNext());
    }

    @Override
    public void addCommentToRecipe(@NonNull Long id, @NonNull AddRecipeCommentRequest comment) throws DataDoesNotExistException, IllegalDataInRequestException {
        checkIfRecipeExists(id);

        if (!StringUtils.hasText(comment.getText())) {
            LOG.error("Comment text is null or empty");
            throw new IllegalDataInRequestException("Comment text can not be null or empty");
        }

        String username = authenticationService.getAuthenticatedUsername();

        RecipeCommentPK recipeCommentPK = new RecipeCommentPK(id, username, LocalDateTime.now(ZoneId.of("UTC")));
        RecipeComment recipeComment = new RecipeComment(recipeCommentPK, comment.getText());

        recipeCommentRepository.save(recipeComment);
    }

    private void checkIfRecipeExists(@NonNull Long id) throws DataDoesNotExistException {
        if (!recipeRepository.existsById(id)) {
            LOG.error("Recipe with id [{}] does not exists in db so there won't be any operation for comments", id);
            throw new DataDoesNotExistException("Recipe with given id does not exists. Id: " + id);
        }
    }
}
