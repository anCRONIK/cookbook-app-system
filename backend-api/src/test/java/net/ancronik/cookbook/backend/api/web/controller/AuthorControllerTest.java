package net.ancronik.cookbook.backend.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.domain.service.AuthorService;
import net.ancronik.cookbook.backend.api.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthorController.class)
@Tag(TestTypes.UNIT)
public class AuthorControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GET_AUTHOR_BY_ID_PATH_PREFIX = "/api/v1/authors/";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService mockAuthorService;

    @BeforeAll
    public static void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    void getAuthorById_AuthorServiceThrowsAnError_ReturnInternalServerError() {
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
    void getAuthorById_EntryNotFound_ReturnNotFound() {
        when(mockAuthorService.getAuthor("roki")).thenThrow(new DataDoesNotExistException("msg"));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_AUTHOR_BY_ID_PATH_PREFIX + "roki"))
            .andExpect(status().isNotFound())
            .andReturn();

        verify(mockAuthorService).getAuthor("roki");
        verifyNoMoreInteractions(mockAuthorService);
    }

    @Test
    @SneakyThrows
    void getAuthorById_EntryFound_ReturnData() {
        AuthorModel mockData = DtoMockData.generateRandomMockDataForAuthorModel(1).get(0);

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

}
