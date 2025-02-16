package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Status;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "feature.service", havingValue = "file")
public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 username 또는 email입니다.");
        }

        User user = new User(request.username(), request.email());
        if (request.profileImage() != null) {
            user.updateProfileImage(request.profileImage());
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatus.updateStatus(Status.CONNECTED);
        user.updateUserStatus(userStatus);

        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getStatus(), user.getProfileImage());
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getProfileImage()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> findUserById(UUID userId) {
        return Optional.ofNullable(userRepository.getUserById(userId))
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getProfileImage()
                ));
    }

    @Override
    public Optional<UserResponse> updateUser(UpdateUserRequest request) {
        return Optional.ofNullable(userRepository.getUserById(request.id()))
                .map(user -> {
                    if (request.username() != null) {
                        user.updateUsername(request.username());
                    }
                    if (request.profileImage() != null) {
                        user.updateProfileImage(request.profileImage());
                    }

                    userRepository.save(user);

                    return new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getStatus(),
                            user.getProfileImage()
                    );
                });
    }

    @Override
    public void deleteUser(UUID userId) {
        Optional.ofNullable(userRepository.getUserById(userId)).ifPresent(user -> {
            // 관련된 데이터 삭제
            binaryContentRepository.delete(user.getProfileImage());
            userStatusRepository.deleteByUserId(user.getId());

            userRepository.delete(user.getId());
        });
    }

    @Override
    public synchronized void updateReadStatus(UUID userId, UUID channelId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");

        user.getReadStatuses().compute(channelId, (key, status) -> {
            if (status == null) return new ReadStatus(userId, channelId);
            status.setUpdatedAt(Instant.now().toEpochMilli());
            return status;
        });

        userRepository.save(user);
    }
}

