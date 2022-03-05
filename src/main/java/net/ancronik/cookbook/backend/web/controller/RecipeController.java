package net.ancronik.cookbook.backend.web.controller;

import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Slice<RecipePreviewDto> getAllRecipes(Pageable pageable) {
        Slice<RecipePreviewDto> data = recipeService.getAllRecipes(pageable);

        if (null == data) {
            throw new EmptyDataException("Empty page returned by service");
        }

        return data;
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeDto> findRecipeById(@PathVariable Long id) {
        Optional<RecipeDto> data = recipeService.getRecipe(id);

        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(data.get());
    }

    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Slice<RecipePreviewDto> getAllRecipesForCategory(@PathVariable String category, Pageable pageable) {
        Slice<RecipePreviewDto> data = recipeService.getAllRecipesForCategory(category, pageable);

        if (null == data) {
            throw new EmptyDataException("Empty page returned by service");
        }

        return data;
    }

}
