package net.ancronik.cookbook.backend.application.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Serializer for {@link Slice}.
 * <p>
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


        jsonGenerator.writeObjectFieldStart("links");

        //Problem is that do not know which endpoint is called :(

        UriTemplate uriTemplate = null;

        uriTemplate = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipes(null)).withSelfRel().getTemplate();
        //uriTemplate = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipesForCategory(null, null)).withSelfRel().getTemplate();

        writeLink(jsonGenerator, createLink(uriTemplate, slice.getPageable(), LinkRelation.of("self")));
        writeLink(jsonGenerator, createLink(uriTemplate, PageRequest.of(0, slice.getSize(), slice.getSort()), LinkRelation.of("firstPage")));
        if (slice.getNumber() > 0) {
            writeLink(jsonGenerator, createLink(uriTemplate, PageRequest.of(slice.getNumber() - 1, slice.getSize(), slice.getSort()), LinkRelation.of("previousPage")));
        }
        if (slice.hasNext()) {
            writeLink(jsonGenerator, createLink(uriTemplate, PageRequest.of(slice.getNumber() + 1, slice.getSize(), slice.getSort()), LinkRelation.of("nextPage")));
        }

        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }

    @SneakyThrows
    private void writeLink(JsonGenerator jsonGenerator, Link link) {
        jsonGenerator.writeStringField(link.getRel().value(), link.toUri().toString());
    }

    private Link createLink(UriTemplate base, Pageable pageable, LinkRelation relation) {
        HateoasPageableHandlerMethodArgumentResolver pageableArgumentResolver = new HateoasPageableHandlerMethodArgumentResolver();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand(new Object[0]));
        pageableArgumentResolver.enhance(builder, null, pageable);
        return Link.of(UriTemplate.of(builder.build().toString()), relation);
    }

}