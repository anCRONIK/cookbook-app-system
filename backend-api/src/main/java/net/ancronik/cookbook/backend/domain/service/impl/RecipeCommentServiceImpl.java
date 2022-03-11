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
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

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

    private final RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentModel> recipeCommentModelAssembler;

    private final RecipeCommentRepository recipeCommentRepository;

    private final RecipeRepository recipeRepository;

    private final AuthenticationService authenticationService;

    @Autowired
    public RecipeCommentServiceImpl(RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentModel> recipeCommentModelAssembler,
                                    RecipeCommentRepository recipeCommentRepository, RecipeRepository recipeRepository,
                                    AuthenticationService authenticationService) {
        this.recipeCommentModelAssembler = recipeCommentModelAssembler;
        this.recipeCommentRepository = recipeCommentRepository;
        this.recipeRepository = recipeRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Slice<RecipeCommentModel> getCommentsForRecipe(@NonNull Long id, @PageableDefault Pageable pageable) throws DataDoesNotExistException {
        LOG.debug("Searching comments for recipe [{}] with pageable [{}]", id, pageable);

        checkIfRecipeExists(id);

        Slice<RecipeComment> data = recipeCommentRepository.findAllByRecipeCommentPKRecipeId(id, pageable);

        List<RecipeCommentModel> dtoList = data.getContent().stream().map(recipeCommentModelAssembler::toModel).collect(Collectors.toList());

        return new SliceImpl<>(dtoList, data.getPageable(), data.hasNext());
    }

    @Override
    public void addCommentToRecipe(@NonNull Long id, @NonNull AddRecipeCommentRequest comment)
            throws DataDoesNotExistException {
        LOG.debug("Adding new comment to [{}] with data [{}]", id, comment);

        checkIfRecipeExists(id);

        String username = authenticationService.getAuthenticatedUsername();

        RecipeCommentPK recipeCommentPK = new RecipeCommentPK(id, username, LocalDateTime.now(ZoneId.of("UTC")));
        RecipeComment recipeComment = new RecipeComment(recipeCommentPK, comment.getText());

        recipeCommentRepository.save(recipeComment);
    }

    private void checkIfRecipeExists(Long id) throws DataDoesNotExistException {
        if (!recipeRepository.existsById(id)) {
            LOG.error("Recipe with id [{}] does not exists in db so there won't be any operation for comments", id);
            throw new DataDoesNotExistException("Recipe with given id does not exists. Id: " + id);
        }
    }
}
