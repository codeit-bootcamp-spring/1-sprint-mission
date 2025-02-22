package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID userId) {
        User deletedUser = data.remove(userId);
        if (deletedUser != null) log.info("닉네임 {}는 사라집니다.", deletedUser.getName());
    }

    @Override
    public boolean existsById(UUID id){
        return data.containsKey(id);
    }

    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }
}
