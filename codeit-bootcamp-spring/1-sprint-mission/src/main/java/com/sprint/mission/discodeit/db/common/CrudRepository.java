package com.sprint.mission.discodeit.db.common;

import java.io.Serializable;
import java.util.Optional;

public interface CrudRepository<T, ID extends Serializable>
        extends Repository<T, ID>{

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Long count();

    void deleteById(ID id);

    boolean existsById(ID id);
}
