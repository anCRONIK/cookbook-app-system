package net.ancronik.cookbook.backend.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.application.Util;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.*;
import net.ancronik.cookbook.backend.web.dto.recipe.*;
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

import static org.junit.jupiter.api.Assertions.*;
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

    @MockBean
    private RecipeCommentService mockRecipeCommentService;

    @MockBean
    private CodeQueryService mockCodeQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private static final String GET_RECIPES_PATH = "/api/v1/recipes";
    private static final String GET_RECIPE_BY_ID_PATH_PREFIX = "/api/v1/recipes/";
    private static final String GET_RECIPES_IN_CATEGORY_PATH_PREFIX = "/api/v1/recipes/categories/";
    private static final String GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE = "/api/v1/recipes/%s/comments";
    private static final String CREATE_RECIPE_PATH = "/api/v1/recipes";
    private static final String ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE = "/api/v1/recipes/%s/comments";
    private static final String UPDATE_RECIPE_PATH_PREFIX = "/api/v1/recipes/";
    private static final String DELETE_RECIPE_PATH_PREFIX = "/api/v1/recipes/";
    private static final String GET_RECIPES_CATEGORIES_PATH = "/api/v1/recipes/categories";

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getRecipes(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getRecipes(any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andReturn();

        verify(mockRecipeService).getRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(1);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH))
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
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(10);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH))
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
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_CallersUsesPaging_ProperParametersSentToService() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(6);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData, Pageable.ofSize(6).withPage(2), false));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH)
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
        assertEquals(2, pageable.getPageNumber());
        assertEquals(6, pageable.getPageSize());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipes_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(2);
        when(mockRecipeService.getRecipes(any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(1, 2, Sort.by(new Sort.Order(Sort.Direction.ASC, "difficulty"),
                        new Sort.Order(Sort.Direction.DESC, "category"))), true));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_PATH)
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
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/api/v1/recipes?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/recipes?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.prev.href").value("http://localhost/api/v1/recipes?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.first.href").value("http://localhost/api/v1/recipes?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getRecipes(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertEquals(1, pageable.getPageNumber());
        assertEquals(2, pageable.getPageSize());
        assertTrue(pageable.getSort().getOrderFor("category").getDirection().isDescending());
        assertTrue(pageable.getSort().getOrderFor("difficulty").getDirection().isAscending());
        assertNull(pageable.getSort().getOrderFor("rating"));
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipeById_GivenIdIsNotAANumber_ReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + "abcd12"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipeById_RecipeServiceThrowsAnError_ReturnInternalServerError() {
        when(mockRecipeService.getRecipe(12L)).thenThrow(new RuntimeException("test"));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + 12))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipe(12L);
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipeById_EntryNotFound_ReturnNotFound() {
        when(mockRecipeService.getRecipe(12L)).thenThrow(new DataDoesNotExistException("msg"));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + 12))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockRecipeService).getRecipe(12L);
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipeById_EntryFound_ReturnData() {
        RecipeDto mockData = DtoMockData.generateRandomMockDataForRecipeDto(1).get(0);

        when(mockRecipeService.getRecipe(mockData.getId())).thenReturn(mockData);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + mockData.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(mockData.getId()))
                .andExpect(jsonPath("$.title").value(mockData.getTitle()))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpectAll(createMatchersForIngredients(mockData))
                .andReturn();

        verify(mockRecipeService).getRecipe(mockData.getId());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getRecipesForCategory(anyString(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abc"))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getRecipesForCategory(anyString(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abch"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(jsonPath("$._links.self").isNotEmpty())
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(1);
        when(mockRecipeService.getRecipesForCategory(eq(mockData.get(0).getCategory()), any()))
                .thenReturn(new SliceImpl<>(mockData, PageRequest.of(0, 20, Sort.unsorted()), false));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.numberOfElements").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getRecipesForCategory_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeBasicInfoDto> mockData = DtoMockData.generateRandomMockDataForRecipeBasicInfoDto(2);
        when(mockRecipeService.getRecipesForCategory(eq(mockData.get(0).getCategory()), any()))
                .thenReturn(new SliceImpl<>(mockData, PageRequest.of(0, 20, Sort.unsorted()), false));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.recipes").isArray())
                .andExpect(jsonPath("$._embedded.recipes").isNotEmpty())
                .andExpectAll(createMatchersForRecipeBasicInfoDto(mockData))
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.hasNext").value(false))
                .andReturn();
        //FIXME links for prev and first not present?

        verify(mockRecipeService).getRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
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
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/api/v1/recipes/categories/" + mockData.get(0).getCategory() + "?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/recipes/categories/" + mockData.get(0).getCategory() + "?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.prev.href").value("http://localhost/api/v1/recipes/categories/" + mockData.get(0).getCategory() + "?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$._links.first.href").value("http://localhost/api/v1/recipes/categories/" + mockData.get(0).getCategory() + "?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.numberOfElements").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getRecipesForCategory(eq(mockData.get(0).getCategory()), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertEquals(1, pageable.getPageNumber());
        assertEquals(2, pageable.getPageSize());
        assertTrue(pageable.getSort().getOrderFor("difficulty").getDirection().isDescending());
        assertNull(pageable.getSort().getOrderFor("rating"));
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 1L)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), any());
        verifyNoInteractions(mockRecipeService);
        verifyNoMoreInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceThrowsNotFoundException_ReturnNotFound() {
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenThrow(new DataDoesNotExistException("null"));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 1L)))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), any());
        verifyNoInteractions(mockRecipeService);
        verifyNoMoreInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(GET_COMMENTS_FOR_RECIPE_PATH_TEMPLATE, 1L)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links").isNotEmpty())
                .andExpect(jsonPath("$._links.self").isNotEmpty())
                .andReturn();

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(1);
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData));

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

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(4);
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData));

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

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), any());
        verifyNoInteractions(mockRecipeService);
        verifyNoMoreInteractions(mockRecipeCommentService);
    }

    @Test
    @SneakyThrows
    public void getCommentsForRecipe_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipeCommentDto> mockData = DtoMockData.generateRandomMockDataForRecipeCommentDto(4);
        when(mockRecipeCommentService.getCommentsForRecipe(anyLong(), any())).thenReturn(new SliceImpl<>(mockData,
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

        verify(mockRecipeCommentService).getCommentsForRecipe(anyLong(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertEquals(10, pageable.getPageNumber());
        assertEquals(12, pageable.getPageSize());
        assertTrue(pageable.getSort().getOrderFor("dateCreated").getDirection().isDescending());
        verifyNoInteractions(mockRecipeService);
        verifyNoMoreInteractions(mockRecipeCommentService);
    }


    @SneakyThrows
    @Test
    public void createRecipe_NoDataInRequest_ServiceThrowsException_ReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_NoFullDataInRequest_ServiceThrowsException_ReturnBadRequest() {
        when(mockRecipeService.createRecipe(any())).thenThrow(new IllegalDataInRequestException("no data"));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockRecipeService).createRecipe(any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void createRecipe_ValidRequest_ReturnCreatedEntry() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();

        when(mockRecipeService.createRecipe(any())).thenReturn(DtoMockData.generateRandomMockDataForRecipeDto(1).get(0));//FIXME

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_RECIPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andReturn();

        verify(mockRecipeService).createRecipe(eq(request));
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_NoDataInRequest_ReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andReturn();


        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_RecipeNotFound_ReturnBadRequest() {
        AddRecipeCommentRequest request = new AddRecipeCommentRequest("dssadaskldjaskldjklas");

        doThrow(new DataDoesNotExistException("test")).when(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isNotFound())
                .andReturn();


        verify(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_IllegalDataInRequest_ReturnBadRequest() {
        AddRecipeCommentRequest request = new AddRecipeCommentRequest("dssadaskldjaskldjklas");

        doThrow(new IllegalDataInRequestException("test")).when(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andReturn();


        verify(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void addCommentToRecipe_RequestIsValid_ReturnOk() {
        AddRecipeCommentRequest request = new AddRecipeCommentRequest("kjdklas jdksajdlaksjd djskald jkl");

        doNothing().when(mockRecipeCommentService).addCommentToRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(ADD_COMMENT_TO_RECIPE_PATH_TEMPLATE, 123))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isOk())
                .andReturn();


        verify(mockRecipeCommentService).addCommentToRecipe(eq(123L),eq(request));
        verifyNoMoreInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_ServiceThrowsRuntimeException_ReturnInternalServerError() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
        doThrow(new RuntimeException("random")).when(mockRecipeService).updateRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "231")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).updateRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_RecipeNotFound_ReturnNotFound() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
        doThrow(new DataDoesNotExistException("random")).when(mockRecipeService).updateRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "231")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockRecipeService).updateRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_UpdateDataInvalid_ReturnBadRequest() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
        doThrow(new IllegalDataInRequestException("random")).when(mockRecipeService).updateRecipe(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "231")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockRecipeService).updateRecipe(anyLong(), any());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_UpdateDataValid_ReturnUpdatedEntry() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
        when(mockRecipeService.updateRecipe(anyLong(), any())).thenReturn(DtoMockData.generateRandomMockDataForRecipeDto(1).get(0));

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RECIPE_PATH_PREFIX + "231")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andReturn();

        verify(mockRecipeService).updateRecipe(eq(231L), eq(request));
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void deleteRecipe_ServiceThrowsRuntimeException_ReturnInternalServerError() {
        doThrow(new RuntimeException("random")).when(mockRecipeService).deleteRecipe(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RECIPE_PATH_PREFIX + "231"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).deleteRecipe(anyLong());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }


    @SneakyThrows
    @Test
    public void deleteRecipe_RecipeNotFound_ReturnNotFound() {
        doThrow(new DataDoesNotExistException("no")).when(mockRecipeService).deleteRecipe(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RECIPE_PATH_PREFIX + "231"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockRecipeService).deleteRecipe(anyLong());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void deleteRecipe_RecipeDeletedSuccessfully_ReturnOk() {
        doNothing().when(mockRecipeService).deleteRecipe(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_RECIPE_PATH_PREFIX + "231"))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockRecipeService).deleteRecipe(anyLong());
        verifyNoMoreInteractions(mockRecipeService);
        verifyNoInteractions(mockRecipeCommentService);
    }

    @SneakyThrows
    @Test
    public void getRecipeCategories_ServiceThrowsGenericDatabaseException_ReturnInternalServerError() {
        when(mockCodeQueryService.getRecipeCategories()).thenThrow(new GenericDatabaseException("test"));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_CATEGORIES_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockCodeQueryService).getRecipeCategories();
        verifyNoMoreInteractions(mockCodeQueryService);
        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }


    @SneakyThrows
    @Test
    public void getRecipeCategories_ServiceReturnsData_CheckResponse() {
        when(mockCodeQueryService.getRecipeCategories())
                .thenReturn(List.of(new RecipeCategoryDto("dessert"), new RecipeCategoryDto("entree")));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_CATEGORIES_PATH))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.categories").isArray())
                .andExpect(jsonPath("$._embedded.categories.length()").value(2))
                .andReturn();

        verify(mockCodeQueryService).getRecipeCategories();
        verifyNoMoreInteractions(mockCodeQueryService);
        verifyNoInteractions(mockRecipeCommentService);
        verifyNoInteractions(mockRecipeService);
    }

    private ResultMatcher[] createMatchersForRecipeBasicInfoDto(List<RecipeBasicInfoDto> data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data && !data.isEmpty()) {
            for (int i = 0, len = data.size(); i < len; ++i) {
                matchers.add(jsonPath("$._embedded.recipes").exists());
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].id").value(data.get(i).getId()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].title").value(data.get(i).getTitle()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].shortDescription").value(data.get(i).getShortDescription()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].thumbnailUrl").value(data.get(i).getThumbnailUrl()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].dateCreated").value(Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getDateCreated())));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].difficulty").value(data.get(i).getDifficulty()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].preparationTime").value(data.get(i).getPreparationTime()));
                matchers.add(jsonPath("$._embedded.recipes[" + i + "].cookingTime").value(data.get(i).getCookingTime()));
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
