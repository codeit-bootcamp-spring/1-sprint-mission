package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service("fileUserStatusService")
public class FileUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    public FileUserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

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
        userStatusRepository.deleteById(userId);
    }
}
