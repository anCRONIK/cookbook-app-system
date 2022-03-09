package net.ancronik.cookbook.backend.web.controller;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.web.dto.DtoMockData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MeasurementUnitController.class)
@Tag(TestTypes.UNIT)
public class MeasurementUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeQueryService mockCodeQueryService;


    @SneakyThrows
    @Test
    public void getMeasurementUnits_ServiceThrowsGenericDatabaseException_ReturnInternalServerError() {
        when(mockCodeQueryService.getMeasurementUnits()).thenThrow(new GenericDatabaseException("test"));

        mockMvc.perform(MockMvcRequestBuilders.get(MeasurementUnitController.DEFAULT_MAPPING))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        verify(mockCodeQueryService).getMeasurementUnits();
        verifyNoMoreInteractions(mockCodeQueryService);
    }


    @SneakyThrows
    @Test
    public void getMeasurementUnits_ServiceReturnsData_CheckResponse() {
        when(mockCodeQueryService.getMeasurementUnits()).thenReturn(DtoMockData.generateRandomMockDataForMeasurementUnitModel(4));

        mockMvc.perform(MockMvcRequestBuilders.get(MeasurementUnitController.DEFAULT_MAPPING))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.units").isArray())
                .andExpect(jsonPath("$._embedded.units.length()").value(4))
                .andExpect(jsonPath("$._embedded.units").isNotEmpty())
                .andReturn();

        verify(mockCodeQueryService).getMeasurementUnits();
        verifyNoMoreInteractions(mockCodeQueryService);
    }

}
