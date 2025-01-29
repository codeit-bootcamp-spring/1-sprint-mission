package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface CRUDService<T> {
    T create(T entity);
    T readOne(UUID id);
    List<T> readAll();
    T update(UUID id, T entity);
    boolean delete(UUID id);
}
