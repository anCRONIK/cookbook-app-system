package net.ancronik.cookbook.backend.api.web.filter;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.Constants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@Tag(TestTypes.UNIT)
public class RequestResponseClidFilterTest {

    private final RequestResponseClidFilter filter = new RequestResponseClidFilter();

    @SneakyThrows
    @Test
    public void doFilter_ClidNotInRequest_GenerateNewOne(){
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        doNothing().when(mockFilterChain).doFilter(servletRequest, servletResponse);

        filter.doFilter(servletRequest, servletResponse, mockFilterChain);

        assertTrue(servletResponse.containsHeader(Constants.HEADER_CLID));
        assertNotNull(servletResponse.getHeader(Constants.HEADER_CLID));
    }


    @SneakyThrows
    @Test
    public void doFilter_ClidInRequest_ReturnInResponse(){
        String clid = UUID.randomUUID().toString();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        doNothing().when(mockFilterChain).doFilter(servletRequest, servletResponse);

        servletRequest.addHeader(Constants.HEADER_CLID, clid);

        filter.doFilter(servletRequest, servletResponse, mockFilterChain);

        assertTrue(servletResponse.containsHeader(Constants.HEADER_CLID));
        assertEquals(clid, servletResponse.getHeader(Constants.HEADER_CLID));
    }
}
