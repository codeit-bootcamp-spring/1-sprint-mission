package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    void updateLastSeen(UUID userId, Instant timestamp);
    Optional<UserStatus> getUserStatus(UUID userId);
    boolean isUserOnline(UUID userId);
    void removeUserStatus(UUID userId); // ✅ 추가된 메서드
}
