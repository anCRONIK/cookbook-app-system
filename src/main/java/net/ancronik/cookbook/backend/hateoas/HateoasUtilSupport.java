package net.ancronik.cookbook.backend.hateoas;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing methods to help with Spring HATEOAS.
 *
 * @author Nikola Presecki
 */
public class HateoasUtilSupport {

    /**
     * Method that will create basic links for {@link net.ancronik.cookbook.backend.hateoas.SlicedModel}.
     * <p>
     * For getting template, something like this can be used {@code WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipes(null)).withSelfRel().getTemplate())}.
     *
     * @param slice slice which contains data
     * @param base  template for which method this is used
     * @return array of links which size depends on if there are previous and next one
     */
    public static Link[] createLinksForSlicedModel(Slice<?> slice, UriTemplate base) {
        HateoasPageableHandlerMethodArgumentResolver pageableArgumentResolver = new HateoasPageableHandlerMethodArgumentResolver();

        List<Link> list = new ArrayList<>();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableArgumentResolver.enhance(builder, null, slice.getPageable());
        list.add(Link.of(UriTemplate.of(builder.build().toString()), IanaLinkRelations.SELF));

        if (slice.getPageable().isPaged()) {

            builder = UriComponentsBuilder.fromUri(base.expand());
            pageableArgumentResolver.enhance(builder, null, PageRequest.of(0, slice.getPageable().getPageSize(), slice.getPageable().getSort()));
            list.add(Link.of(UriTemplate.of(builder.build().toString()), IanaLinkRelations.FIRST));

            if (slice.getNumber() > 0) {
                builder = UriComponentsBuilder.fromUri(base.expand());
                pageableArgumentResolver.enhance(builder, null, PageRequest.of(slice.getPageable().getPageNumber() - 1, slice.getPageable().getPageSize(), slice.getPageable().getSort()));
                list.add(Link.of(UriTemplate.of(builder.build().toString()), IanaLinkRelations.PREV));
            }

            if (slice.hasNext()) {
                builder = UriComponentsBuilder.fromUri(base.expand());
                pageableArgumentResolver.enhance(builder, null, PageRequest.of(slice.getPageable().getPageNumber() + 1, slice.getPageable().getPageSize(), slice.getPageable().getSort()));
                list.add(Link.of(UriTemplate.of(builder.build().toString()), IanaLinkRelations.NEXT));
            }
        }

        return list.toArray(Link[]::new);
    }

}
