package net.ancronik.cookbook.backend.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.domain.service.AuthorService;
import net.ancronik.cookbook.backend.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorDto;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthorController.class)
@Tag("unit")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService mockAuthorService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String GET_AUTHOR_BY_ID_PATH_PREFIX = "/api/v1/authors/";
    private static final String CREATE_AUTHOR_PATH = "/api/v1/authors";
    private static final String UPDATE_AUTHOR_PATH_PREFIX = "/api/v1/authors/";

    @BeforeAll
    public static void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    public void getAuthorById_AuthorServiceThrowsAnError_ReturnInternalServerError() {
        when(mockAuthorService.getAuthor("roki")).thenThrow(new RuntimeException("test"));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_AUTHOR_BY_ID_PATH_PREFIX + "roki"))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockAuthorService).getAuthor("roki");
        verifyNoMoreInteractions(mockAuthorService);
    }

    @Test
    @SneakyThrows
    public void getAuthorById_EntryNotFound_ReturnNotFound() {
        when(mockAuthorService.getAuthor("roki")).thenThrow(new DataDoesNotExistException("msg"));
        mockMvc.perform(MockMvcRequestBuilders.get(GET_AUTHOR_BY_ID_PATH_PREFIX + "roki"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockAuthorService).getAuthor("roki");
        verifyNoMoreInteractions(mockAuthorService);
    }

    @Test
    @SneakyThrows
    public void getAuthorById_EntryFound_ReturnData() {
        AuthorDto mockData = DtoMockData.generateRandomMockDataForAuthorDto(1).get(0);

        when(mockAuthorService.getAuthor(mockData.getUsername())).thenReturn(mockData);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_AUTHOR_BY_ID_PATH_PREFIX + mockData.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.username").value(mockData.getUsername()))
                .andExpect(jsonPath("$.bio").value(mockData.getBio()))
                .andExpect(jsonPath("$.dateOfBirth").value(mockData.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.fullName").value(mockData.getFullName()))
                .andExpect(jsonPath("$.imageUrl").value(mockData.getImageUrl()))
                .andReturn();

        verify(mockAuthorService).getAuthor(mockData.getUsername());
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void createAuthor_NoDataInRequest_ServiceThrowsException_ReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void createAuthor_NoFullDataInRequest_ServiceThrowsException_ReturnBadRequest() {
        when(mockAuthorService.createAuthor(any())).thenThrow(new IllegalDataInRequestException("no data"));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockAuthorService).createAuthor(any());
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void createAuthor_ValidRequest_ReturnCreatedEntry() {
        AuthorCreateRequest request = new AuthorCreateRequest("testni_user", LocalDate.of(2022, 3, 7));

        when(mockAuthorService.createAuthor(any())).thenReturn(new AuthorDto(request.getUsername(), null, request.getDateOfBirth(), null, null).add(Link.of("test", IanaLinkRelations.SELF)));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andReturn();

        verify(mockAuthorService).createAuthor(eq(request));
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_ServiceThrowsRuntimeException_ReturnInternalServerError() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();
        doThrow(new RuntimeException("random")).when(mockAuthorService).updateAuthor(anyString(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_AUTHOR_PATH_PREFIX + "testUser")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockAuthorService).updateAuthor(anyString(), any());
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_RecipeNotFound_ReturnNotFound() {
        AuthorUpdateRequest request = new AuthorUpdateRequest("NovoIme", null, "Super opis", StringTestUtils.generateRandomUrl());
        doThrow(new DataDoesNotExistException("random")).when(mockAuthorService).updateAuthor(anyString(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_AUTHOR_PATH_PREFIX + "testUser")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(mockAuthorService).updateAuthor(eq("testUser"), eq(request));
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_UpdateDataInvalid_ReturnBadRequest() {
        AuthorUpdateRequest request = new AuthorUpdateRequest("NovoIme", null, "Super opis", StringTestUtils.generateRandomUrl());
        doThrow(new IllegalDataInRequestException("random")).when(mockAuthorService).updateAuthor(anyString(), any());

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_AUTHOR_PATH_PREFIX + "testUser")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockAuthorService).updateAuthor(eq("testUser"), eq(request));
        verifyNoMoreInteractions(mockAuthorService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_UpdateDataValid_ReturnUpdatedEntry() {
        AuthorUpdateRequest request = new AuthorUpdateRequest("NovoIme", null, "Super opis", StringTestUtils.generateRandomUrl());
        when(mockAuthorService.updateAuthor(anyString(), any()))
                .thenReturn(new AuthorDto("testUser", request.getFullName(), request.getDateOfBirth(), request.getBio(),
                        null).add(Link.of("test", IanaLinkRelations.SELF)));

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_AUTHOR_PATH_PREFIX + "testUser")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        verify(mockAuthorService).updateAuthor(eq("testUser"), eq(request));
        verifyNoMoreInteractions(mockAuthorService);
    }

}
