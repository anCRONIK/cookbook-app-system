package net.ancronik.cookbook.backend.application.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.Link;

import java.io.IOException;

/**
 * Serializer for {@link Slice}.
 * <p>
 * It customizes our output to allow us to control how JSON is formatted
 *
 * @author Nikola Presecki
 */
@JsonComponent
public class SlicedModelJacksonSerializer extends JsonSerializer<SlicedModel<?>> {

    @Override
    public void serialize(SlicedModel model, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("data", model.getContent());


        jsonGenerator.writeNumberField("numberOfElements", model.getMetadata().getNumberOfElements());
        jsonGenerator.writeNumberField("pageSize", model.getMetadata().getSize());
        jsonGenerator.writeNumberField("pageNumber", model.getMetadata().getNumber());
        jsonGenerator.writeBooleanField("hasNext", model.getMetadata().isHasNext());

        jsonGenerator.writeObjectFieldStart("links");

        for(Link link : model.getLinks()){
            jsonGenerator.writeStringField(link.getRel().value(), link.getHref());
        }

        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}