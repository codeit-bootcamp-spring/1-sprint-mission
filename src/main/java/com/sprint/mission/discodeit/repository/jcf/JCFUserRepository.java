package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {
    private final Map<Long, User> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(User user) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, user);
        return id;
    }

    public void saveUser(Long id, User user) {
        data.put(id, user);
    }

    @Override
    public User load(Long id) {
        return data.get(id);
    }

    @Override
    public Map.Entry<Long, User> load(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    public Map<Long, User> loadAll() {
        return data;
    }

    @Override
    public void delete(Long id) {
        if (data.remove(id) == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public void update(Long id, User user) {
        data.put(id, user);
    }

    @Override
    public boolean isExistByUserName(String userName) {
        return loadAll().values().stream()
                .anyMatch(entry -> entry.getUserName().getName().equals(userName));
    }

    @Override
    public boolean isExistByEmail(String email) {
        return loadAll().values().stream()
                .anyMatch(entry -> entry.getEmail().getEmail().equals(email));
    }

    @Override
    public boolean confirmLogin(String userName, String password) {
        return loadAll().values().stream()
                .anyMatch(entry ->
                        entry.getUserName().getName().equals(userName) &&
                                entry.getPassword().getPasswd().equals(password));
    }

    @Override
    public Map.Entry<Long, User> findUserByUserName(String userName) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getUserName().getName().equals(userName))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
