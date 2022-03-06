package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.application.exceptions.Util;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import net.ancronik.cookbook.backend.web.dto.RecipeCommentDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RecipeController.class)
@Tag("unit")
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService mockRecipeService;


    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private static final String GET_ALL_RECIPES_PATH = "/api/v1/recipes";
    private static final String GET_RECIPE_BY_ID_PATH_PREFIX = "/api/v1/recipes/";
    private static final String GET_RECIPES_IN_CATEGORY_PATH_PREFIX = "/api/v1/recipes/category/";
    private static final String GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE = "/api/v1/recipes/%s/comments";

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getRecipes(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getRecipes(any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(1);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.numberOfElements").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(10);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.recipes").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.numberOfElements").value(10))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_CallersUsesPaging_ProperParametersSentToService() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(6);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData, Pageable.ofSize(6).withPage(2), false));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH)
                        .queryParam("page", "2").queryParam("size", "6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.recipes").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(6))
                .andExpect(jsonPath("$.page.numberOfElements").value(6))
                .andExpect(jsonPath("$.page.number").value(2))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andExpect(jsonPath("$._links.nextPage").doesNotExist())
                .andReturn();

        verify(mockRecipeService).getRecipes(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(2, pageable.getPageNumber());
        Assertions.assertEquals(6, pageable.getPageSize());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(2);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(1, 2, Sort.by(new Sort.Order(Sort.Direction.ASC, "difficulty"),
                        new Sort.Order(Sort.Direction.DESC, "category"))), true));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH)
                        .queryParam("page", "1").queryParam("size", "2")
                        .queryParam("sort", "category,DESC")
                        .queryParam("sort", "difficulty,ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/api/v1/recipes/?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/recipes/?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.prev.href").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.first.href").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getRecipes(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(1, pageable.getPageNumber());
        Assertions.assertEquals(2, pageable.getPageSize());
        Assertions.assertTrue(pageable.getSort().getOrderFor("category").getDirection().isDescending());
        Assertions.assertTrue(pageable.getSort().getOrderFor("difficulty").getDirection().isAscending());
        Assertions.assertNull(pageable.getSort().getOrderFor("rating"));
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void findRecipeById_GivenIdIsNotAANumber_ReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + "abcd12"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();

        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void findRecipeById_RecipeServiceThrowsAnError_ReturnInternalServerError() {
        when(mockRecipeService.getRecipe(12L)).thenThrow(new RuntimeException("test"));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + 12))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipe(12L);
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void findRecipeById_EntryNotFound_Return404() {
        when(mockRecipeService.getRecipe(12L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + 12))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockRecipeService).getRecipe(12L);
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void findRecipeById_EntryFound_ReturnData() {
        RecipeDto mockData = DtoMockData.generateRandomMockDataForRecipeDto(1).get(0);

        when(mockRecipeService.getRecipe(mockData.getId())).thenReturn(Optional.of(mockData));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + mockData.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(mockData.getId()))
                .andExpect(jsonPath("$.title").value(mockData.getTitle()))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients").isNotEmpty())
                .andExpectAll(createMatchersForIngredients(mockData))
                .andReturn();

        verify(mockRecipeService).getRecipe(mockData.getId());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getRecipesForCategory(anyString(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getRecipesForCategory(anyString(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abch"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.page").doesNotExist())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(jsonPath("$._links.self").isNotEmpty())
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(1);
        when(mockRecipeService.getRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.numberOfElements").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(2);
        when(mockRecipeService.getRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(2);
        when(mockRecipeService.getRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(1, 2, Sort.by(new Sort.Order(Sort.Direction.ASC, "difficulty"),
                        new Sort.Order(Sort.Direction.DESC, "category"))), true));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory())
                        .queryParam("page", "1").queryParam("size", "2")
                        .queryParam("sort", "difficulty,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/api/v1/recipes/category/" + mockData.get(0).getCategory() + "?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/recipes/category/" + mockData.get(0).getCategory() + "?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.prev.href").value("http://localhost/api/v1/recipes/category/" + mockData.get(0).getCategory() + "?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.first.href").value("http://localhost/api/v1/recipes/category/" + mockData.get(0).getCategory() + "?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(eq(mockData.get(0).getCategory()), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(1, pageable.getPageNumber());
        Assertions.assertEquals(2, pageable.getPageSize());
        Assertions.assertTrue(pageable.getSort().getOrderFor("difficulty").getDirection().isDescending());
        Assertions.assertNull(pageable.getSort().getOrderFor("rating"));
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getCommentsForRecipe(anyLong(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 1L)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getCommentsForRecipe(anyLong(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 1L)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.page").doesNotExist())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(jsonPath("$._links.self").isNotEmpty())
                .andReturn();

        verify(mockRecipeService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(1);
        when(mockRecipeService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 23L)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeCommentDto(mockData))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.numberOfElements").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(4);
        when(mockRecipeService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 23L)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeCommentDto(mockData))
                .andExpect(jsonPath("$.page.size").value(4))
                .andExpect(jsonPath("$.page.numberOfElements").value(4))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(4);
        when(mockRecipeService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(10, 12, Sort.by(new Sort.Order(Sort.Direction.DESC, "dateCreated"))), false));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 23L))
                        .queryParam("page", "10").queryParam("size", "12")
                        .queryParam("sort", "dateCreated,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeCommentDto(mockData))
                .andExpect(jsonPath("$.page.size").value(12))
                .andExpect(jsonPath("$.page.numberOfElements").value(4))
                .andExpect(jsonPath("$.page.number").value(10))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getCommentsForRecipe(anyLong(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(10, pageable.getPageNumber());
        Assertions.assertEquals(12, pageable.getPageSize());
        Assertions.assertTrue(pageable.getSort().getOrderFor("dateCreated").getDirection().isDescending());
        verifyNoMoreInteractions(mockRecipeService);
    }


    private ResultMatcher[] createMatchersForRecipeBasicInfoDto(List<RecipeBasicInfoDto> data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data && !data.isEmpty()) {
            for (int i = 0, len = data.size(); i < len; ++i) {
                matchers.add(jsonPath("$._embedded.recipes").exists());
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].id").value(data.get(i).getId()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].title").value(data.get(i).getTitle()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].shortDescription").value(data.get(i).getShortDescription()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].coverImageUrl").value(data.get(i).getCoverImageUrl()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].dateCreated").value(Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getDateCreated())));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].difficulty").value(data.get(i).getDifficulty()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].category").value(data.get(i).getCategory()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].authorId").value(data.get(i).getAuthorId()));
            }
        }
        return matchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] createMatchersForIngredients(RecipeDto data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data.getIngredientList() && !data.getIngredientList().isEmpty()) {
            for (int i = 0, len = data.getIngredientList().size(); i < len; ++i) {
                matchers.add(jsonPath("$.ingredients[" + i + "].name").value(data.getIngredientList().get(i).getName()));
                matchers.add(jsonPath("$.ingredients[" + i + "].quantity").value(data.getIngredientList().get(i).getQuantity()));
                matchers.add(jsonPath("$.ingredients[" + i + "].measurementUnit").value(data.getIngredientList().get(i).getMeasurementUnit()));
            }
        }
        return matchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] createMatchersForRecipeCommentDto(List<RecipeCommentDto> data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data && !data.isEmpty()) {
            for (int i = 0, len = data.size(); i < len; ++i) {
                matchers.add(jsonPath("$._embedded.comments").exists());
                matchers.add(jsonPath("$._embedded.comments[" + i + "].username").value(data.get(i).getUsername()));
                matchers.add(jsonPath("$._embedded.comments[" + i + "].text").value(data.get(i).getText()));
                matchers.add(jsonPath("$._embedded.comments[" + i + "].dateCreated").value(Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getDateCreated())));
            }
        }
        return matchers.toArray(ResultMatcher[]::new);
    }

}
