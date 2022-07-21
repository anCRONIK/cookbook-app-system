package net.ancronik.cookbook.backend.api.web.filter;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.Constants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@Tag(TestTypes.UNIT)
class RequestResponseClidFilterTest {

    private final RequestResponseClidFilter filter = new RequestResponseClidFilter();
    private final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    private final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
    @Mock
    private FilterChain mockFilterChain;

    @SneakyThrows
    @Test
    void doFilter_ClidNotInRequest_GenerateNewOne() {
        doNothing().when(mockFilterChain).doFilter(servletRequest, servletResponse);

        filter.doFilter(servletRequest, servletResponse, mockFilterChain);

        assertTrue(servletResponse.containsHeader(Constants.HEADER_CLID));
        assertNotNull(servletResponse.getHeader(Constants.HEADER_CLID));
    }


    @SneakyThrows
    @Test
    void doFilter_ClidInRequest_ReturnInResponse() {
        String clid = UUID.randomUUID().toString();
        doNothing().when(mockFilterChain).doFilter(servletRequest, servletResponse);

        servletRequest.addHeader(Constants.HEADER_CLID, clid);

        filter.doFilter(servletRequest, servletResponse, mockFilterChain);

        assertTrue(servletResponse.containsHeader(Constants.HEADER_CLID));
        assertEquals(clid, servletResponse.getHeader(Constants.HEADER_CLID));
    }
}
