package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ValidateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateUser validateUser;

    @Override
    public UserDto create(UserCreateRequest request) {
        validateUser.validateUser(request.username(), request.email(), request.phoneNumber(), request.password());

        User user = new User(request.username(), request.email(), request.phoneNumber(), request.password());
        userRepository.save(user);
        UserStatus userStatus = new UserStatus(user.getUserId(), Instant.now());
        userStatusRepository.save(userStatus);
        // profileImage가 있을 경우 binaryContent에 저장
        if (request.profileImage() != null) {
            BinaryContent binaryContent = new BinaryContent(user.getUserId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }
        // Dto로 반환
        return changeToDto(user, userStatus.isOnline());
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        boolean isOnline = userStatusRepository.findByUserId(user.getUserId())
                .map(UserStatus::isOnline)
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found."));
        return changeToDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> changeToDto(user, userStatusRepository.findByUserId(user.getUserId())
                        .map(UserStatus::isOnline)
                        .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found."))))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserUpdateRequest request) {
        validateUser.validateUser(request.username(), request.email(), request.phoneNumber(), request.password());

        User user = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.update(request.username(), request.email(), request.phoneNumber(), request.password());
        if (request.profileImage() != null) {
            binaryContentRepository.findProfileByUserId(user.getUserId())
                    .ifPresent(binaryContent -> binaryContentRepository.deleteByContentId(binaryContent.getUserId()));
            BinaryContent binaryContent = new BinaryContent(user.getUserId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }

        boolean isOnline = userStatusRepository.findByUserId(user.getUserId())
                .map(UserStatus::isOnline)
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found."));
        return changeToDto(user, isOnline);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private UserDto changeToDto(User user, boolean isOnline) {
        return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), isOnline, user.getCreatedAt(), user.getUpdatedAt());
    }
}
