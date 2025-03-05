package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserReadResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service("fileUserService")
@RequiredArgsConstructor
public class FileUserService {

    private final Map<UUID, User> userData = new HashMap<>();
    private final Map<UUID, UserStatus> userStatusData = new HashMap<>();

    public void create(UserCreateRequest userDTO) {
        if (existsByUsername(userDTO.getUsername()) || existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        UUID userId = UUID.randomUUID();
        UUID profileImageId = null;
        if (userDTO.getProfileImageId() != null && !userDTO.getProfileImageId().isEmpty()) {
            profileImageId = UUID.fromString(userDTO.getProfileImageId());
        }

        // User 생성자: User(UUID, String, String, UUID, String)
        User user = new User(userId, userDTO.getUsername(), userDTO.getEmail(), profileImageId, userDTO.getPassword());
        userData.put(userId, user);

        userStatusData.put(userId, new UserStatus(userId, Instant.now()));
    }

    public Optional<UserReadResponse> read(UUID id) {
        return Optional.ofNullable(userData.get(id)).map(user -> {
            UserStatus status = userStatusData.get(id);
            boolean isOnline = status != null && status.isOnline();
            // 순서: id, username, email, profileImageId, lastActive, online
            return new UserReadResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getProfileImageId(),
                    status != null ? status.getLastActiveAt() : null,
                    isOnline
            );
        });
    }

    public List<UserReadResponse> readAll() {
        return userData.values().stream()
                .map(user -> {
                    UserStatus status = userStatusData.get(user.getId());
                    boolean isOnline = status != null && status.isOnline();
                    return new UserReadResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getProfileImageId(),
                            status != null ? status.getLastActiveAt() : null,
                            isOnline
                    );
                })
                .collect(Collectors.toList());
    }

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
