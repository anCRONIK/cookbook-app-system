package net.ancronik.cookbook.backend.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;

public class AbstractSteps {

    protected static ResponseResults latestResponse = null;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected Map<String, String> generateDefaultHttpHeaders() {
        return new HashMap<>();
    }
}