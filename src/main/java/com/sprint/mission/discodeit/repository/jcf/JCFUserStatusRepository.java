package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        data = new HashMap<>(100);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 UserStatus입니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }
}
