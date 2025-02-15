package com.sprint.mission.discodeit.repository;

import java.util.UUID;

public interface UserStatusRepository {
    void delete(UUID id);
    // findByUserId(UUID userId) 구현
}
