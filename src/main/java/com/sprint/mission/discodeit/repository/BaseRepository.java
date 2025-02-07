package com.sprint.mission.discodeit.repository;

import java.util.Optional;

public interface BaseRepository<T, ID> {
  Optional<T> findById(ID id);
}
