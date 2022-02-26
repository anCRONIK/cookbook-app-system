package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDtoMockData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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


    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsNull_ReturnServerError() {
        when(mockRecipeService.getAllRecipes(anyInt(), anyInt(), any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/recipes"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockRecipeService).getAllRecipes(anyInt(), anyInt(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsEmptyList_ReturnDataToCaller() {
        when(mockRecipeService.getAllRecipes(anyInt(), anyInt(), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.size").exists())
                .andReturn();

        verify(mockRecipeService).getAllRecipes(anyInt(), anyInt(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }

    @Test
    @SneakyThrows
    public void getAllRecipes_ServiceReturnsMultipleData_ReturnDataToCaller() {
        List<RecipeDto> mockData = RecipeDtoMockData.generateRandomMockData(10);
        when(mockRecipeService.getAllRecipes(anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(mockData));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.sort").exists())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.size").value(mockData.size()))
                .andReturn();

        verify(mockRecipeService).getAllRecipes(anyInt(), anyInt(), any());
        verifyNoMoreInteractions(mockRecipeService);
    }
}
