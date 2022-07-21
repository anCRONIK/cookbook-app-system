package net.ancronik.cookbook.backend.api.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.StringTestUtils;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.CdnException;
import net.ancronik.cookbook.backend.api.domain.service.CdnService;
import net.ancronik.cookbook.backend.api.web.dto.UploadImageResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UploadImageController.class)
@Tag(TestTypes.UNIT)
class UploadImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CdnService mockCdnService;


    @SneakyThrows
    @Test
    void uploadImage_ServiceCdnException_ReturnInternalServerError() {
        MockMultipartFile file = new MockMultipartFile("imageFile", "file.png", "text/plain", "data".getBytes(StandardCharsets.UTF_8));

        when(mockCdnService.uploadImage(anyBoolean(), any())).thenThrow(new CdnException("test"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(UploadImageController.DEFAULT_MAPPING)
                            .file(file))
            .andExpect(status().isInternalServerError())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.description").exists())
            .andExpect(jsonPath("$.timestamp").exists())
            .andReturn();

        verify(mockCdnService).uploadImage(anyBoolean(), any());
        verifyNoMoreInteractions(mockCdnService);
    }


    @SneakyThrows
    @Test
    void uploadImage_ServiceReturnsData_CheckResponse() {
        MockMultipartFile file = new MockMultipartFile("imageFile", "file.png", "text/plain", "data".getBytes(StandardCharsets.UTF_8));

        when(mockCdnService.uploadImage(anyBoolean(), any())).thenReturn(new UploadImageResponse(StringTestUtils.generateRandomUrl(), StringTestUtils.generateRandomUrl()));

        mockMvc.perform(MockMvcRequestBuilders.multipart(UploadImageController.DEFAULT_MAPPING)
                            .file(file))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.image").exists())
            .andExpect(jsonPath("$.thumbnail").exists())
            .andReturn();

        verify(mockCdnService).uploadImage(anyBoolean(), any());
        verifyNoMoreInteractions(mockCdnService);
    }

}
