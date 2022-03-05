package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.application.exceptions.Util;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDtoMockData;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
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

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getAllRecipes(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getAllRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getAllRecipes(any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        verify(mockRecipeService).getAllRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(1);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getAllRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(10);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(10))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getAllRecipes(any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_CallersUsesPaging_ProperParametersSentToService() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(6);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData, Pageable.ofSize(6).withPage(2), false));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH)
                        .queryParam("page", "2").queryParam("size", "6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.pageSize").value(6))
                .andExpect(jsonPath("$.numberOfElements").value(6))
                .andExpect(jsonPath("$.pageNumber").value(2))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.links.nextPage").doesNotExist())
                .andReturn();

        verify(mockRecipeService).getAllRecipes(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(2, pageable.getPageNumber());
        Assertions.assertEquals(6, pageable.getPageSize());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(2);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(1, 2, Sort.by(new Sort.Order(Sort.Direction.ASC, "difficulty"),
                        new Sort.Order(Sort.Direction.DESC, "category"))), true));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH)
                        .queryParam("page", "1").queryParam("size", "2")
                        .queryParam("sort", "category,DESC")
                        .queryParam("sort", "difficulty,ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.links.nextPage").value("http://localhost/api/v1/recipes/?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.self").value("http://localhost/api/v1/recipes/?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.previousPage").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.firstPage").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getAllRecipes(pageableCaptor.capture());
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
        RecipeDto mockData = RecipeDtoMockData.generateRandomMockDataForRecipeDto(1).get(0);

        when(mockRecipeService.getRecipe(mockData.getId())).thenReturn(Optional.of(mockData));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPE_BY_ID_PATH_PREFIX + mockData.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(mockData.getId()))
                .andExpect(jsonPath("$.title").value(mockData.getTitle()))
                .andReturn();

        verify(mockRecipeService).getRecipe(mockData.getId());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipesForCategory_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getAllRecipesForCategory(anyString(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getAllRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipesForCategory_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getAllRecipesForCategory(anyString(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + "abch"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        verify(mockRecipeService).getAllRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipesForCategory_ServiceReturnsOneEntry_ReturnDataToCaller() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(1);
        when(mockRecipeService.getAllRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getAllRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipesForCategory_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(2);
        when(mockRecipeService.getAllRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andReturn();

        verify(mockRecipeService).getAllRecipesForCategory(anyString(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipesForCategory_CallersUsesPagingAndSorting_ProperParametersSentToService() {
        List<RecipePreviewDto> mockData = RecipeDtoMockData.generateRandomMockDataForRecipePreviewDto(2);
        when(mockRecipeService.getAllRecipesForCategory(eq(mockData.get(0).getCategory()), any())).thenReturn(new SliceImpl<>(mockData,
                PageRequest.of(1, 2, Sort.by(new Sort.Order(Sort.Direction.ASC, "difficulty"),
                        new Sort.Order(Sort.Direction.DESC, "category"))), true));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_RECIPES_IN_CATEGORY_PATH_PREFIX + mockData.get(0).getCategory())
                        .queryParam("page", "1").queryParam("size", "2")
                        .queryParam("sort", "difficulty,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.links.nextPage").value("http://localhost/api/v1/recipes/?page=2&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.self").value("http://localhost/api/v1/recipes/?page=1&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.previousPage").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.links.firstPage").value("http://localhost/api/v1/recipes/?page=0&size=2&sort=difficulty,asc&sort=category,desc"))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andReturn();

        verify(mockRecipeService).getAllRecipesForCategory(eq(mockData.get(0).getCategory()), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        Assertions.assertEquals(1, pageable.getPageNumber());
        Assertions.assertEquals(2, pageable.getPageSize());
        Assertions.assertTrue(pageable.getSort().getOrderFor("difficulty").getDirection().isDescending());
        Assertions.assertNull(pageable.getSort().getOrderFor("rating"));
        verifyNoMoreInteractions(mockRecipeService);
    }


    private ResultMatcher[] createMatchersForData(List<RecipePreviewDto> data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data && !data.isEmpty()) {
            for (int i = 0, len = data.size(); i < len; ++i) {
                matchers.add(jsonPath("$.data").exists());
                matchers.add(jsonPath("$.data[" + i + "].id").value(data.get(i).getId()));
                matchers.add(jsonPath("$.data[" + i + "].title").value(data.get(i).getTitle()));
                matchers.add(jsonPath("$.data[" + i + "].shortDescription").value(data.get(i).getShortDescription()));
                matchers.add(jsonPath("$.data[" + i + "].coverImageUrl").value(data.get(i).getCoverImageUrl()));
                matchers.add(jsonPath("$.data[" + i + "].dateCreated").value(Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getDateCreated())));
                matchers.add(jsonPath("$.data[" + i + "].difficulty").value(data.get(i).getDifficulty()));
                matchers.add(jsonPath("$.data[" + i + "].category").value(data.get(i).getCategory()));
                matchers.add(jsonPath("$.data[" + i + "].authorId").value(data.get(i).getAuthorId()));
            }
        }
        return matchers.toArray(ResultMatcher[]::new);
    }

}
