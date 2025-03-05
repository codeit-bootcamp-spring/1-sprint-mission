package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service("jcfUserStatusService")
@RequiredArgsConstructor
public class JCFUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public void updateLastSeen(UUID userId, Instant timestamp) {
        Optional<UserStatus> existingStatus = userStatusRepository.findByUserId(userId);
        existingStatus.ifPresentOrElse(
                status -> {
                    status.setLastActiveAt(timestamp);
                    userStatusRepository.save(status);
                },
                () -> userStatusRepository.save(new UserStatus(userId, timestamp))
        );
    }

    @Override
    public Optional<UserStatus> getUserStatus(UUID userId) {
        return userStatusRepository.findByUserId(userId);
    }

    @Override
    public boolean isUserOnline(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .map(status -> status.getLastActiveAt().isAfter(Instant.now().minusSeconds(300)))
                .orElse(false);
    }

    @Override
    public void removeUserStatus(UUID userId) {
        userStatusRepository.deleteById(userId); // ✅ 미구현된 메서드 추가
    }
}
