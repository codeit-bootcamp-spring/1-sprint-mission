package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList;


    public JCFUserRepository() {
        this.userList = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User findbyId(UUID id) {
        User user = userList.get(id);
        if (user == null) {
            throw new IllegalArgumentException("해당 객체가 존재하지 않습니다.");
        }
        return user;
    }

    @Override
    public Map<UUID, User> load() {
        return userList;
    }

    @Override
    public List<UUID> findAllUserIdByChannelId(UUID uuid) {
        //채널아이디 대상, 속해있는 유저 반환
        return List.of();
    }

    @Override
    public Boolean existByUserId(UUID userId) {
        return load().values().stream()
                .anyMatch(user-> user.getId().equals(userId));
    }

    @Override
    public boolean existsByUsername(String username) {
        return load().values().stream()
                .anyMatch(user -> user.getUserName().equals(username));
    }


    @Override
    public void delete(UUID id) {
        userList.remove(id);
    }
}
