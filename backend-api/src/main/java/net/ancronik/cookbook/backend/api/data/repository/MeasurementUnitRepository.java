package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Db repository for {@link MeasurementUnit}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface MeasurementUnitRepository extends CassandraRepository<MeasurementUnit, String> {

    @Override
    default <S extends MeasurementUnit> List<S> saveAll(Iterable<S> entites) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends MeasurementUnit> S insert(S entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends MeasurementUnit> List<S> insert(Iterable<S> entities) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default <S extends MeasurementUnit> S save(S entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteById(String s) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void delete(MeasurementUnit entity) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAllById(Iterable<? extends String> strings) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll(Iterable<? extends MeasurementUnit> entities) {
        throw new UnsupportedOperationException("not supported for this repository");
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("not supported for this repository");
    }
}
