package some_path._1sprintmission.discodeit.repository.jcf;

import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;
import some_path._1sprintmission.discodeit.repository.UserStatusRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> statusStore = new ConcurrentHashMap<>();


    public void updateStatus(User user) {
        statusStore.put(user.getId(), new UserStatus(user, Instant.now()));
    }

    @Override
    public void deleteById(UUID userId) {

    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        return null;
    }

    @Override
    public Optional<UserStatus> findById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public List<UserStatus> findAll() {
        return null;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, Instant newLastSeenAt) {
        return null;
    }
}
