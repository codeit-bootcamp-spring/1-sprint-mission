package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonUserRepository extends JsonRepository<UUID, User> implements UserRepository {
    public JsonUserRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "users.json",
                new TypeReference<HashMap<UUID, User>>() {}
        );
    }

    @Override
    public User userSave(User user) {
        return save(user.getId(), user);
    }

    @Override
    public User findUserById(UUID userId) {
        return findById(userId);
    }

    @Override
    public Map<UUID, User> findAllUser() {
        return findAll();
    }

    @Override
    public void removeUserById(UUID userId) {
        removeById(userId);
    }
}
