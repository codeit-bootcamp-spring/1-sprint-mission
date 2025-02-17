package some_path._1sprintmission.discodeit.repository;

import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void deleteById(UUID userId);
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID userId);
    List<UserStatus> findAll();
    UserStatus updateByUserId(UUID userId, Instant newLastSeenAt);
}
