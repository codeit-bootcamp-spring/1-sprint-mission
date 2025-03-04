package com.spirnt.mission.discodeit.repository.jcf;

import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Map<UUID, UserStatus> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
