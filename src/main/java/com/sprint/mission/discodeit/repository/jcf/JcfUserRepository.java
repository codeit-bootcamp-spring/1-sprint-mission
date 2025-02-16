package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.form.UserUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class JcfUserRepository implements UserRepository {

    private static final Map<UUID, User> data = new HashMap<>();

    @Override
    public User createUser(UUID id, User user) {
        log.info("save: user={}", user);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        User findUser = data.get(id);
        findUser.setUserName(userParam.getUserName());
        findUser.setUserEmail(userParam.getUserEmail());
        findUser.setLoginId(userParam.getLoginId());
        findUser.setPassword(userParam.getPassword());
        findUser.setAttachProfile(userParam.getAttachProfile());
        findUser.setUpdatedAt();
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByloginId(String loginId) {
        List<User> all=findAll();
        for (User user : all) {
            if(user.getLoginId().equals(loginId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }


}
