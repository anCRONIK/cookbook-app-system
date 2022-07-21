package net.ancronik.cookbook.backend.acceptance.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.ancronik.cookbook.backend.acceptance.AbstractSteps;
import net.ancronik.cookbook.backend.acceptance.HeaderSettingRequestCallback;
import net.ancronik.cookbook.backend.acceptance.ResponseResultErrorHandler;
import net.ancronik.cookbook.backend.acceptance.ResponseResults;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericSteps extends AbstractSteps {

    @When("^the client calls (.+) with http method (.+)$")
    public void theClientCallsApi(String path, String method) {
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(generateDefaultHttpHeaders());
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        testRestTemplate.getRestTemplate().setErrorHandler(errorHandler);
        latestResponse = testRestTemplate.execute(path, HttpMethod.valueOf(method), requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    @Then("^the client receives status code of (\\d+)$")
    public void checkTheStatusCode(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertEquals(statusCode, currentStatusCode.value());
    }

}
