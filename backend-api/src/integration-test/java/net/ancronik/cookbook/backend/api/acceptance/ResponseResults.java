package net.ancronik.cookbook.backend.api.acceptance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class ResponseResults {

    private final ClientHttpResponse theResponse;

    private final String body;

    public ResponseResults(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();

        this.body = StreamUtils.copyToString(bodyInputStream, StandardCharsets.UTF_8);
    }

}