package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service("basicUserService")
@Primary
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final Map<UUID, User> userData = new HashMap<>();
    private final Map<UUID, UserStatus> userStatusData = new HashMap<>();

    @Override
    public void create(UserCreateDTO userDTO) {
        if (existsByUsername(userDTO.getUsername()) || existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        UUID userId = UUID.randomUUID();
        User user = new User(userId, userDTO.getUsername(), userDTO.getEmail(), userDTO.getProfileImageId(), userDTO.getPassword());
        userData.put(userId, user);

        userStatusData.put(userId, new UserStatus(userId, Instant.now()));
    }

    @Override
    public Optional<UserReadDTO> read(UUID id) {
        return Optional.ofNullable(userData.get(id)).map(user -> {
            UserStatus status = userStatusData.get(id);
            boolean isOnline = status != null && status.isOnline();
            return new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageId(), isOnline, status != null ? status.getLastActiveAt() : null);
        });
    }

    @Override
    public List<UserReadDTO> readAll() {
        return userData.values().stream()
                .map(user -> {
                    UserStatus status = userStatusData.get(user.getId());
                    boolean isOnline = status != null && status.isOnline();
                    return new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageId(), isOnline, status != null ? status.getLastActiveAt() : null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(UUID id, UserUpdateDTO userDTO) {
        User user = userData.get(id);
        if (user != null) {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setProfileImageId(userDTO.getProfileImageId());
        }
    }

    @Override
    public void delete(UUID id) {
        userData.remove(id);
        userStatusData.remove(id);
    }

    public boolean existsByUsername(String username) {
        return userData.values().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean existsByEmail(String email) {
        return userData.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
