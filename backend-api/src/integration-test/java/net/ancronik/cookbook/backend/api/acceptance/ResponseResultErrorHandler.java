package net.ancronik.cookbook.backend.api.acceptance;

import lombok.NonNull;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ResponseResultErrorHandler implements ResponseErrorHandler {
    public ResponseResults results = null;

    public Boolean hadError = false;

    public ResponseResults getResults() {
        return results;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        hadError = response.getRawStatusCode() >= 400;
        return hadError;
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) throws IOException {
        results = new ResponseResults(response);
    }
}