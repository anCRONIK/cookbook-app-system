package net.ancronik.cookbook.backend.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractSteps {

    protected static ResponseResults latestResponse = null;

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    protected TestRestTemplate testRestTemplate;


    protected Map<String, String> generateDefaultHttpHeaders() {
        return new HashMap<>();
    }
}