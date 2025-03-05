package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<Long, UserStatus> data;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(UserStatus userStatus) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, userStatus);
        return id;
    }

    @Override
    public UserStatus load(Long id) {
        return data.get(id);

    }

    @Override
    public Map.Entry<Long, UserStatus> load(UUID uuid) {
        // TODO: ErrorCode 추가
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    @Override
    public Map<Long, UserStatus> loadAll() {
        return data;
    }

    @Override
    public Long delete(Long id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("삭제할 User Status가 존재하지 않습니다.");
        }
        return id;
    }

    @Override
    public void update(Long id, UserStatus userStatus) {
        data.put(id, userStatus);
    }

    @Override
    public Map.Entry<Long, UserStatus> findUserStatusByUserId(UUID userId) {
        // TODO: ErrorCode 추가
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void deleteByUserId(UUID userId) {
        loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userId))
                .findFirst() // 첫 번째(유일한) UserStatus만 선택
                .ifPresent(entry -> delete(entry.getKey())); // 존재하면 삭제
    }
}
