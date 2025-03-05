package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service("basicUserStatusService")
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public void updateLastSeen(UUID userId, Instant timestamp) {
        Optional<UserStatus> existingStatus = userStatusRepository.findByUserId(userId);

        if (existingStatus.isPresent()) {
            UserStatus status = existingStatus.get();
            status.setLastActiveAt(timestamp);
            userStatusRepository.save(status);
        } else {
            userStatusRepository.save(new UserStatus(userId, timestamp));
        }
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
        userStatusRepository.deleteById(userId);
    }
}
