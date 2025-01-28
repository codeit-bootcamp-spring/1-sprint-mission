package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }

    /**
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from {@code userInfoToCreate}
     */
    @Override
    public User createUser(User userInfoToCreate) {
        User userToCreate = new User.Builder(
                userInfoToCreate.getName(), userInfoToCreate.getEmail())
                .id(userInfoToCreate.getId())
                .phoneNumber(userInfoToCreate.getPhoneNumber())
                .build();

        return Optional.ofNullable(data.putIfAbsent(userToCreate.getId(), userToCreate))
                .map(existingUser -> User.createEmptyUser())
                .orElse(userToCreate);
    }

    @Override
    public User findUserById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(User.createEmptyUser());
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code userInfoToUpdate}
     */
    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        User exsitingUser = findUserById(key);
        User userToUpdate = new User.Builder(
                userInfoToUpdate.getName(), userInfoToUpdate.getEmail())
                .id(key)
                .createAt(exsitingUser.getCreateAt())
                .phoneNumber(userInfoToUpdate.getPhoneNumber())
                .build();

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, user) -> userToUpdate))
                .orElse(User.createEmptyUser());
    }

    @Override
    public User deleteUserById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(User.createEmptyUser());
    }
}
