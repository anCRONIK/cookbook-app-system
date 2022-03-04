package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.MockTestUtils;
import net.ancronik.cookbook.backend.application.exceptions.Util;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDtoMockData;
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

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getAllRecipes(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
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
        List<RecipeDto> mockData = RecipeDtoMockData.generateRandomMockData(1);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.sort").exists())
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
        List<RecipeDto> mockData = RecipeDtoMockData.generateRandomMockData(10);
        when(mockRecipeService.getAllRecipes(any())).thenReturn(new SliceImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_RECIPES_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpectAll(createMatchersForData(mockData))
                .andExpect(jsonPath("$.sort").exists())
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
        List<RecipeDto> mockData = RecipeDtoMockData.generateRandomMockData(6);
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
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.pageSize").value(6))
                .andExpect(jsonPath("$.numberOfElements").value(6))
                .andExpect(jsonPath("$.pageNumber").value(2))
                .andExpect(jsonPath("$.hasNext").value(false))
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
        List<RecipeDto> mockData = RecipeDtoMockData.generateRandomMockData(2);
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
                .andExpect(jsonPath("$.sort").isArray())
                .andExpect(jsonPath("$.sort[0].property").value("difficulty"))
                .andExpect(jsonPath("$.sort[0].direction").value("ASC"))
                .andExpect(jsonPath("$.sort[1].property").value("category"))
                .andExpect(jsonPath("$.sort[1].direction").value("DESC"))
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


    private ResultMatcher[] createMatchersForData(List<RecipeDto> data) {
        List<ResultMatcher> matchers = new ArrayList<>();

        if (null != data && !data.isEmpty()) {
            for (int i = 0, len = data.size(); i < len; ++i) {
                matchers.add(jsonPath("$.data").exists());
                matchers.add(jsonPath("$.data[" + i + "].id").value(data.get(i).getId()));
                matchers.add(jsonPath("$.data[" + i + "].title").value(data.get(i).getTitle()));
                matchers.add(jsonPath("$.data[" + i + "].shortDescription").value(data.get(i).getShortDescription()));
                matchers.add(jsonPath("$.data[" + i + "].coverImageUrl").value(data.get(i).getCoverImageUrl()));
                matchers.add(jsonPath("$.data[" + i + "].ingredientList").isArray());
                matchers.add(jsonPath("$.data[" + i + "].preparationTime").value(data.get(i).getPreparationTime()));
                matchers.add(jsonPath("$.data[" + i + "].preparationInstructions").value(data.get(i).getPreparationInstructions()));
                matchers.add(jsonPath("$.data[" + i + "].cookTime").value(data.get(i).getCookTime()));
                matchers.add(jsonPath("$.data[" + i + "].cookingInstructions").value(data.get(i).getCookingInstructions()));
                matchers.add(jsonPath("$.data[" + i + "].dateCreated").value(Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getDateCreated())));
                matchers.add(jsonPath("$.data[" + i + "].lastUpdated").value(null == data.get(i).getLastUpdated() ? null : Util.APP_DATE_TIME_FORMATTER.format(data.get(i).getLastUpdated())));
                matchers.add(jsonPath("$.data[" + i + "].difficulty").value(data.get(i).getDifficulty()));
                matchers.add(jsonPath("$.data[" + i + "].category").value(data.get(i).getCategory()));
                matchers.add(jsonPath("$.data[" + i + "].authorId").value(data.get(i).getAuthorId()));

                int ingCnt = 0;
                final int index = i;
                if (null != data.get(i).getIngredientList() && !data.get(i).getIngredientList().isEmpty()) {
                    matchers.addAll(MockTestUtils.convertMatrixToList(data.get(i).getIngredientList().stream().map(
                            (ing) -> new ResultMatcher[]{
                                    jsonPath("$.data[" + index + "].ingredientList[" + ingCnt + "].name").value(data.get(index).getIngredientList().get(ingCnt).getName()),
                                    jsonPath("$.data[" + index + "].ingredientList[" + ingCnt + "].quantity").value(data.get(index).getIngredientList().get(ingCnt).getQuantity()),
                                    jsonPath("$.data[" + index + "].ingredientList[" + ingCnt + "].measurementUnit").value(data.get(index).getIngredientList().get(ingCnt).getMeasurementUnit())
                            }
                    ).toArray(ResultMatcher[][]::new)));
                }
            }
        }
        return matchers.toArray(ResultMatcher[]::new);
    }

}
