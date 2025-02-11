package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

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
}
