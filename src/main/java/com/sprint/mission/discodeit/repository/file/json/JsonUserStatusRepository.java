package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonUserStatusRepository extends JsonRepository<UUID, UserStatus> implements UserStatusRepository {

    public JsonUserStatusRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "userStatus.json",
                new TypeReference<HashMap<UUID, UserStatus>>() {}
        );
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        if (userStatus == null) {
            throw new IllegalUserException("유저를 저장하는데 문제가 생겼습니다.");
        }

        map.put(userStatus.getUserId(), userStatus);
        saveToJson();
        return map.get(userStatus.getUserId());
    }

    @Override
    public UserStatus findById(UUID userId) {
         return map.get(userId);
    }

    @Override
    public Map<UUID, UserStatus> findAll() {
        return map;
    }

    @Override
    public UserStatus updateState(UUID userId, UserStatus userStatus) {
        UserStatus findState = findById(userId);
        if (findState == null) {
            throw new IllegalUserException("updateState 실패");
        }

        findState.updateUserStatus();

        map.put(findState.getUserId(), findState);

        return map.get(findState.getUserId());
    }

    @Override
    public void remove(UUID userId) {
        if(userId == null) {
            throw new IllegalUserException("삭제할 UserStatus가 없습니다.");
        }

        map.remove(userId);
    }
}
