package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple mapper interface.
 *
 * @author Nikola Presecki
 */
public interface Mapper<R, S> {

    S map(@NonNull R r);

    default List<S> mapList(@NonNull List<R> list) {
        return list.stream().map(this::map).collect(Collectors.toList());
    }

}
