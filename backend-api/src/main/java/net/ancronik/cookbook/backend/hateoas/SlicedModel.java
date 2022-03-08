package net.ancronik.cookbook.backend.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Hateoas support for {@link org.springframework.data.domain.Slice}.
 *
 * @author Nikola Presecki
 */
@SuppressWarnings("NullableProblems")
public class SlicedModel<T> extends CollectionModel<T> {
    /**
     * Instance without page.
     */
    public static SlicedModel<?> NO_PAGE = new SlicedModel<>();

    private final SlicedModel.SliceMetadata metadata;

    private final ResolvableType fallbackType;

    /**
     * Default constructor to allow instantiation by reflection.
     */
    protected SlicedModel() {
        this(new ArrayList<>(), null);
    }

    protected SlicedModel(Collection<T> content, @Nullable SliceMetadata metadata) {
        this(content, metadata, Links.NONE);
    }

    protected SlicedModel(Collection<T> content, @Nullable SliceMetadata metadata, Iterable<Link> links) {
        this(content, metadata, links, null);
    }

    protected SlicedModel(Collection<T> content, @Nullable SliceMetadata metadata, Iterable<Link> links,
                          @Nullable ResolvableType fallbackType) {

        super(content, links, fallbackType);

        this.metadata = metadata;
        this.fallbackType = fallbackType;
    }

    /**
     * Creates an empty {@link SlicedModel}.
     *
     * @return will never be {@literal null}.
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty() {
        return empty(Collections.emptyList());
    }

    /**
     * Creates an empty {@link SlicedModel} with the given fallback type.
     *
     * @param fallbackElementType must not be {@literal null}.
     * @param generics            must not be {@literal null}.
     * @return will never be {@literal null}.
     * @see #withFallbackType(Class, Class...)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(Class<T> fallbackElementType, Class<?> generics) {
        return empty(ResolvableType.forClassWithGenerics(fallbackElementType, generics));
    }

    /**
     * Creates an empty {@link SlicedModel} with the given fallback type.
     *
     * @param fallbackElementType must not be {@literal null}.
     * @return will never be {@literal null}.
     * @see #withFallbackType(ParameterizedTypeReference)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(ParameterizedTypeReference<T> fallbackElementType) {
        return empty(ResolvableType.forType(fallbackElementType));
    }

    /**
     * Creates an empty {@link SlicedModel} with the given fallback type.
     *
     * @param fallbackElementType must not be {@literal null}.
     * @return will never be {@literal null}.
     * @see #withFallbackType(ResolvableType)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(ResolvableType fallbackElementType) {
        return new SlicedModel<>(Collections.emptyList(), null, Collections.emptyList(), fallbackElementType);
    }

    /**
     * Creates an empty {@link SlicedModel} with the given links.
     *
     * @param links must not be {@literal null}.
     * @return model
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty(Link... links) {
        return empty((SliceMetadata) null, links);
    }

    /**
     * Creates an empty {@link SlicedModel} with the given links.
     *
     * @param links must not be {@literal null}.
     * @return model
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty(Iterable<Link> links) {
        return empty((SliceMetadata) null, links);
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata}.
     *
     * @param metadata can be {@literal null}.
     * @return model
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata) {
        return empty(metadata, Collections.emptyList());
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata} and fallback type.
     *
     * @param metadata     can be {@literal null}.
     * @param fallbackType must not be {@literal null}.
     * @param generics     must not be {@literal null}.
     * @return will never be {@literal null}.
     * @see #withFallbackType(Class, Class...)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata, Class<?> fallbackType, Class<?>... generics) {

        Assert.notNull(fallbackType, "Fallback type must not be null!");
        Assert.notNull(generics, "Generics must not be null!");

        return empty(metadata, ResolvableType.forClassWithGenerics(fallbackType, generics));
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata} and fallback type.
     *
     * @param metadata can be {@literal null}.
     * @return model
     * @see #withFallbackType(ParameterizedTypeReference)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata, ParameterizedTypeReference<T> fallbackType) {

        Assert.notNull(fallbackType, "Fallback type must not be null!");

        return empty(metadata, ResolvableType.forType(fallbackType));
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata} and fallback type.
     *
     * @param metadata     can be {@literal null}.
     * @param fallbackType must not be {@literal null}.
     * @return model
     * @see #withFallbackType(ResolvableType)
     * @since 1.4
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata, ResolvableType fallbackType) {

        Assert.notNull(fallbackType, "Fallback type must not be null!");

        return new SlicedModel<>(Collections.emptyList(), metadata, Collections.emptyList(), fallbackType);
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata} and links.
     *
     * @param metadata can be {@literal null}.
     * @param links    must not be {@literal null}.
     * @return model
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata, Link... links) {
        return empty(Arrays.asList(links));
    }

    /**
     * Creates an empty {@link SlicedModel} with the given {@link SliceMetadata} and links.
     *
     * @param metadata can be {@literal null}.
     * @param links    must not be {@literal null}.
     * @return model
     * @since 1.1
     */
    public static <T> SlicedModel<T> empty(@Nullable SliceMetadata metadata, Iterable<Link> links) {
        return of(Collections.emptyList(), metadata, links);
    }


    /**
     * Creates a new {@link SlicedModel} from the given {@link Slice}.
     *
     * @param slice must not be {@literal null}.
     * @return model
     */
    public static <T> SlicedModel<T> of(Slice<T> slice) {
        return new SlicedModel<>(slice.getContent(), SliceMetadata.createFromSlice(slice));
    }

    /**
     * Creates a new {@link SlicedModel} from the given {@link Slice} and {@link Link}s (optional).
     *
     * @param slice must not be {@literal null}.
     * @return model
     */
    public static <T> SlicedModel<T> of(Slice<T> slice, Link... links) {
        return new SlicedModel<>(slice.getContent(), SliceMetadata.createFromSlice(slice), Arrays.asList(links));
    }

    /**
     * Creates a new {@link SlicedModel} from the given content, {@link SliceMetadata} and {@link Link}s (optional).
     *
     * @param content  must not be {@literal null}.
     * @param metadata can be {@literal null}.
     */
    public static <T> SlicedModel<T> of(Collection<T> content, @Nullable SliceMetadata metadata) {
        return new SlicedModel<>(content, metadata);
    }

    /**
     * Creates a new {@link SlicedModel} from the given content, {@link SliceMetadata} and {@link Link}s (optional).
     *
     * @param content  must not be {@literal null}.
     * @param metadata can be {@literal null}.
     * @param links    links
     */
    public static <T> SlicedModel<T> of(Collection<T> content, @Nullable SliceMetadata metadata, Link... links) {
        return new SlicedModel<>(content, metadata, Arrays.asList(links));
    }

    /**
     * Creates a new {@link SlicedModel} from the given content {@link SliceMetadata} and {@link Link}s.
     *
     * @param content  must not be {@literal null}.
     * @param metadata can be {@literal null}.
     * @param links    links
     */
    public static <T> SlicedModel<T> of(Collection<T> content, @Nullable SliceMetadata metadata, Iterable<Link> links) {
        return new SlicedModel<>(content, metadata, links);
    }

    /**
     * Factory method to easily create a {@link SlicedModel} instance from a set of entities and pagination metadata.
     *
     * @param content  must not be {@literal null}.
     * @param metadata metadata
     * @return model
     */
    @SuppressWarnings("unchecked")
    public static <T extends EntityModel<S>, S> SlicedModel<T> wrap(Iterable<S> content, SliceMetadata metadata) {

        Assert.notNull(content, "Content must not be null!");
        ArrayList<T> resources = new ArrayList<>();

        for (S element : content) {
            resources.add((T) EntityModel.of(element));
        }

        return SlicedModel.of(resources, metadata);
    }

    /**
     * Returns the pagination metadata.
     *
     * @return the metadata
     */
    @JsonProperty("page")
    @Nullable
    public SliceMetadata getMetadata() {
        return metadata;
    }

    /**
     * Returns the Link pointing to the next page (if set).
     *
     * @return optional of link
     */
    @JsonIgnore
    public Optional<Link> getNextLink() {
        return getLink(IanaLinkRelations.NEXT);
    }

    /**
     * Returns the Link pointing to the previous page (if set).
     *
     * @return optional of link
     */
    @JsonIgnore
    public Optional<Link> getPreviousLink() {
        return getLink(IanaLinkRelations.PREV);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.CollectionModel#withFallbackType(java.lang.Class, java.lang.Class[])
     */
    @Override
    public SlicedModel<T> withFallbackType(Class<? super T> type, Class<?>... generics) {
        return withFallbackType(ResolvableType.forClassWithGenerics(type, generics));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.CollectionModel#withFallbackType(org.springframework.core.ParameterizedTypeReference)
     */
    @Override
    public SlicedModel<T> withFallbackType(ParameterizedTypeReference<?> type) {
        return withFallbackType(ResolvableType.forType(type));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.CollectionModel#withFallbackType(org.springframework.core.ResolvableType)
     */
    @Override
    public SlicedModel<T> withFallbackType(ResolvableType type) {
        return new SlicedModel<>(getContent(), metadata, getLinks(), type);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.RepresentationModel#toString()
     */
    @Override
    public String toString() {
        return String.format("SlicedModel { content: %s, fallbackType: %s, metadata: %s, links: %s }", //
                getContent(), fallbackType, metadata, getLinks());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.CollectionModel#equals(java.lang.Object)
     */
    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        SlicedModel<?> that = (SlicedModel<?>) obj;

        return Objects.equals(this.metadata, that.metadata) //
                && super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.hateoas.CollectionModel#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(metadata);
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class SliceMetadata {
        @JsonProperty
        private long size;
        @JsonProperty
        private long numberOfElements;
        @JsonProperty
        private long number;
        @JsonProperty
        private boolean hasNext;

        protected SliceMetadata() {
        }

        public SliceMetadata(long size, long number, long numberOfElements, boolean hasNext) {
            Assert.isTrue(size > -1L, "Size must not be negative!");
            Assert.isTrue(number > -1L, "Number must not be negative!");
            Assert.isTrue(numberOfElements > -1L, "Number of elements must not be negative!");
            this.size = size;
            this.number = number;
            this.numberOfElements = numberOfElements;
            this.hasNext = hasNext;
        }

        /**
         * Method for creating metadata using {@link Slice}
         *
         * @param slice slice, can not be {@literal null}
         * @return metadata
         */
        public static SliceMetadata createFromSlice(Slice<?> slice) {
            if (0 == slice.getNumberOfElements()) {
                return null;
            }
            return new SliceMetadata(slice.getSize(), slice.getNumber(), slice.getNumberOfElements(), slice.hasNext());
        }

    }
}

