package net.ancronik.cookbook.backend.application.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.io.IOException;

/**
 * Serializer for {@link Slice}.
 *
 * It customizes our output to allow us to control how JSON is formatted
 *
 * @author Nikola Presecki
 */
@JsonComponent
public class SliceJacksonSerializer extends JsonSerializer<Slice<?>> {

    @Override
    public void serialize(Slice slice, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("data", slice.getContent());

        jsonGenerator.writeNumberField("numberOfElements", slice.getNumberOfElements());
        jsonGenerator.writeNumberField("pageSize", slice.getSize());
        jsonGenerator.writeNumberField("pageNumber", slice.getNumber());
        jsonGenerator.writeBooleanField("hasNext", slice.hasNext());

        Sort sort = slice.getSort();

        jsonGenerator.writeArrayFieldStart("sort");

        for (Sort.Order order : sort) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("property", order.getProperty());
            jsonGenerator.writeStringField("direction", order.getDirection().name());
            jsonGenerator.writeBooleanField("ignoreCase", order.isIgnoreCase());
            jsonGenerator.writeStringField("nullHandling", order.getNullHandling().name());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}