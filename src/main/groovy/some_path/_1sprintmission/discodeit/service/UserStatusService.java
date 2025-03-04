package some_path._1sprintmission.discodeit.service;

import some_path._1sprintmission.discodeit.dto.UserStatusDTO;
import some_path._1sprintmission.discodeit.entiry.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusDTO dto);
    Optional<UserStatus> findById(UUID userId);
    List<UserStatus> findAll();
    UserStatus update(UUID id, UserStatusDTO dto);
    UserStatus updateByUserId(UUID userId, Instant newLastSeenAt);
    void delete(UUID userId);
}
