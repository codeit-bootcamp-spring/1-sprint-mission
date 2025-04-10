package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface BaseRepository<T> {
    boolean save(T data);
    T findById(UUID id);
    List<T> readAll();
    T modify(UUID id, T data);
    boolean deleteById(UUID id);
}
