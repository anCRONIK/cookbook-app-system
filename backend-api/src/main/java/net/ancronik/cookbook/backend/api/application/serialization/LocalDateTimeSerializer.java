package net.ancronik.cookbook.backend.api.application.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.ancronik.cookbook.backend.api.application.Util;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Serializer for {@link LocalDateTime}.
 *
 * @author Nikola Presecki
 */
@JsonComponent
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(Util.APP_DATE_TIME_FORMATTER.format(localDateTime));
    }

}
