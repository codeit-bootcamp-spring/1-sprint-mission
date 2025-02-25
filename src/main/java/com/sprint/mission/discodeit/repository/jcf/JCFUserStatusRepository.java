package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Profile("jcf")
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> repository;

    public JCFUserStatusRepository(FileUserRepository fileUserRepository) {
        this.repository = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        repository.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userID) {
        return repository.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userID))
                .findFirst();
    }

    @Override
    public List<UserStatus> findByIsOnlineTrue() {
        return repository.values().stream()
                .filter(UserStatus::isOnline)  // isOnline()이 true인 값만 필터링
                .toList();
    }


    @Override
    public Optional<UserStatus> findById(UUID id) {
        return repository.values().stream()
                .filter(userStatus -> userStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return repository.values().stream()
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userID) {
        List<UUID> keysToRemove = repository.entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userID))
                .map(Map.Entry::getKey)
                .toList();

        keysToRemove.forEach(repository::remove);
    }
}
