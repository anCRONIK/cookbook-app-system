package net.ancronik.cookbook.backend.domain.service.impl;

import net.ancronik.cookbook.backend.TestConfigurationForUnitTesting;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationForUnitTesting.class)
@Tag(TestTypes.UNIT)
public class RecipeServiceImplValidationTest { //TODO finish me

    @Autowired
    private RecipeService recipeService;


    @Test
    public void getRecipes_InvalidPageableGiven_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipes(Pageable.unpaged()));
        assertEquals("getRecipes.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipes(Pageable.ofSize(110)));
        assertEquals("getRecipes.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());
    }

    @Test
    public void getRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipe(-1L));
        assertEquals("getRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }


//
//    @SneakyThrows
//    @Test
//    public void createRecipe_TitleIsEmpty_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setTitle("");
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.title").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_TitleIsTooLong_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setTitle(StringTestUtils.getRandomStringInLowerCase(51));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.title").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_ShortDescriptionTooLong_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setShortDescription(StringTestUtils.getRandomStringInLowerCase(201));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.shortDescription").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_ThumbnailNotUrl_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setThumbnailUrl("notUrl/notPath");
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.thumbnailUrl").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_CoverImageNotUrl_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setCoverImageUrl("notUrl/notPath");
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.coverImageUrl").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_NoIngredients_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setIngredientList(new ArrayList<>());
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.ingredientList").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_IngredientsListTooLong_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setIngredientList(DtoMockData.generateRandomMockDataForIngredientDto(1001));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.ingredientList").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_PreparationTimeAndInstructionsInvalid_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setPreparationInstructions(StringTestUtils.getRandomStringInLowerCase(20001));
//        request.setPreparationTimeInMinutes(144001);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.preparationInstructions").isNotEmpty())
//                .andExpect(jsonPath("$.errors.preparationTimeInMinutes").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_CookingTimeAndInstructionsInvalid_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setCookingInstructions(StringTestUtils.getRandomStringInLowerCase(100001));
//        request.setCookingTimeInMinutes(1441);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.cookingInstructions").isNotEmpty())
//                .andExpect(jsonPath("$.errors.cookingTimeInMinutes").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_NoCookingInstructions_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setCookingInstructions("");
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.cookingInstructions").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_DifficultyOutOfBoundLow_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setDifficulty(0);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.difficulty").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_DifficultyOutOfBoundHigh_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setDifficulty(6);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.difficulty").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_CategoryTooLong_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setCategory(StringTestUtils.getRandomStringInLowerCase(51));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.category").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void createRecipe_CategoryEmpty_ReturnValidationError() {
//        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();
//        request.setCategory(null);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.category").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void addCommentToRecipe_CommentTooLong_ReturnValidationError() {
//        AddRecipeCommentRequest request = new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(10001));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.text").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void addCommentToRecipe_IdIsNegativeNumber_ReturnValidationError() {
//        AddRecipeCommentRequest request = new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(101));
//
//        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, -123))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.id").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//    }
//
//    @Test
//    @SneakyThrows
//    public void updateRecipe_NegativeRecipeId_ReturnValidationError() {
//        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "-231")
//                        .content(objectMapper.writeValueAsBytes(request))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.id").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//    }
//
//    @Test
//    @SneakyThrows
//    public void deleteRecipe_NegativeRecipeId_ReturnValidationError() {
//        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RECIPE_PATH_PREFIX + (-132)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.errors").isNotEmpty())
//                .andExpect(jsonPath("$.errors.id").isNotEmpty())
//                .andExpect(jsonPath("$.timestamp").exists())
//                .andReturn();
//    }

}
