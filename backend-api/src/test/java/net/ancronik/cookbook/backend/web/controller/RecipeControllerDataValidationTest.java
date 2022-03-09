package net.ancronik.cookbook.backend.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.web.dto.recipe.AddRecipeCommentRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RecipeController.class)
@Tag(TestTypes.UNIT)
public class RecipeControllerDataValidationTest {

    //there are no validation tests for update operation because they are covered by create request

    private static final String CREATE_RECIPE_PATH = "/api/v1/recipes";
    private static final String ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE = "/api/v1/recipes/%s/comments";
    private static final String GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE = "/api/v1/recipes/%s/comments";
    private static final String GET_RECIPE_BY_ID_PATH_PREFIX = "/api/v1/recipes/";
    private static final String DELETE_RECIPE_PATH_PREFIX = "/api/v1/recipes/";
    private static final String UPDATE_RECIPE_PATH_PREFIX = "/api/v1/recipes/";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipeService mockRecipeService;
    @MockBean
    private RecipeCommentService mockRecipeCommentService;
    @MockBean
    private CodeQueryService mockCodeQueryService;

    @Test
    @SneakyThrows
    public void getRecipeById_NegativeRecipeId_ReturnValidationError() {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + (-1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.id").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_NegativeRecipeId_ReturnValidationError() {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, -1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.id").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_TitleIsEmpty_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setTitle("");

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.title").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_TitleIsTooLong_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setTitle(StringTestUtils.getRandomStringInLowerCase(51));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.title").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_ShortDescriptionTooLong_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setShortDescription(StringTestUtils.getRandomStringInLowerCase(201));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.shortDescription").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_ThumbnailNotUrl_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setThumbnailUrl("notUrl/notPath");

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.thumbnailUrl").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_CoverImageNotUrl_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setCoverImageUrl("notUrl/notPath");

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.coverImageUrl").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_NoIngredients_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setIngredientList(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.ingredientList").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_IngredientsListTooLong_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setIngredientList(DtoMockData.generateRandomMockDataForIngredientDto(1001));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.ingredientList").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_PreparationTimeAndInstructionsInvalid_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setPreparationInstructions(StringTestUtils.getRandomStringInLowerCase(20001));
        request.setPreparationTimeInMinutes(144001);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.preparationInstructions").isNotEmpty())
                .andExpect(jsonPath("$.errors.preparationTimeInMinutes").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_CookingTimeAndInstructionsInvalid_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setCookingInstructions(StringTestUtils.getRandomStringInLowerCase(100001));
        request.setCookingTimeInMinutes(1441);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.cookingInstructions").isNotEmpty())
                .andExpect(jsonPath("$.errors.cookingTimeInMinutes").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_NoCookingInstructions_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setCookingInstructions("");

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.cookingInstructions").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_DifficultyOutOfBoundLow_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setDifficulty(0);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.difficulty").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_DifficultyOutOfBoundHigh_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setDifficulty(6);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.difficulty").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_CategoryTooLong_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setCategory(StringTestUtils.getRandomStringInLowerCase(51));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.category").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_CategoryEmpty_ReturnValidationError() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
        request.setCategory(null);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.category").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_CommentTooLong_ReturnValidationError() {
        AddRecipeCommentRequest request = new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(10001));

        doNothing().when(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.text").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();


        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_IdIsNegativeNumber_ReturnValidationError() {
        AddRecipeCommentRequest request = new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(101));

        doNothing().when(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, -123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.id").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();


        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void updateRecipe_NegativeRecipeId_ReturnValidationError() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "-231")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.id").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void deleteRecipe_NegativeRecipeId_ReturnValidationError() {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RECIPE_PATH_PREFIX + (-132)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.id").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

}
