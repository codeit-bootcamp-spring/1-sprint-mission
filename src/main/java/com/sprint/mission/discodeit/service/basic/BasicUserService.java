package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request, Optional<CreateBinaryContentRequest> optionalRequest) {
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 username 또는 email입니다.");
        }

        User user = new User(request.username(), request.password(), request.email());
        optionalRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                }).ifPresent(user::updateProfileImage);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatus.updateStatus();
        user.updateUserStatus(userStatus);

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> findUserById(UUID userId) {
        return Optional.ofNullable(userRepository.getUserById(userId))
                .map(UserResponse::fromEntity);
    }

    @Override
    public Optional<UserResponse> updateUser(UpdateUserRequest request, Optional<CreateBinaryContentRequest> optionalRequest) {
        return Optional.ofNullable(userRepository.getUserById(request.id()))
                .map(user -> {
                    if (request.username() != null) {
                        user.updateUsername(request.username());
                    }
                    optionalRequest
                            .map(profileRequest -> {
                                String fileName = profileRequest.fileName();
                                String contentType = profileRequest.contentType();
                                byte[] bytes = profileRequest.bytes();
                                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                                return binaryContentRepository.save(binaryContent).getId();
                            }).ifPresent(user::updateProfileImage);
                    userRepository.save(user);

                    return UserResponse.fromEntity(user);
                });
    }

    @Override
    public void deleteUser(UUID userId) {
        Optional.ofNullable(userRepository.getUserById(userId)).ifPresent(user -> {
            // 관련된 데이터 삭제
            binaryContentRepository.deleteById(user.getProfileImage());
            userStatusRepository.deleteById(user.getStatus().getId());

            userRepository.deleteById(user.getId());
        });
    }
}
