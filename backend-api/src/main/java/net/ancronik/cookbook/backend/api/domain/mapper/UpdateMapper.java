package net.ancronik.cookbook.backend.api.domain.mapper;

import lombok.NonNull;

/**
 * Simple update mapper interface.
 *
 * @author Nikola Presecki
 */
public interface UpdateMapper<R, S> {

    void update(@NonNull R r, @NonNull S s);

}
