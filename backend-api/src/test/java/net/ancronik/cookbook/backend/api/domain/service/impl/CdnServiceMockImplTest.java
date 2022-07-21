package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(TestTypes.UNIT)
class CdnServiceMockImplTest {

    CdnServiceMockImpl cdnServiceMock = new CdnServiceMockImpl();

    @Test
    void uploadImage_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cdnServiceMock.uploadImage(false, null));
    }

    @SneakyThrows
    @Test
    void uploadImage_MockCaseScenario() {
        assertNotNull(cdnServiceMock.uploadImage(false, new MockMultipartFile("test", "test".getBytes(StandardCharsets.UTF_8))));
    }
}
