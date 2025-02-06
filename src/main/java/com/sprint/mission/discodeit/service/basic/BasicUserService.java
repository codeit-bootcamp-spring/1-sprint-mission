package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public UserDto create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User(request.username(), request.email(), request.phoneNumber(), request.password());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        if (request.profileImage() != null) {
            BinaryContent binaryContent = new BinaryContent(user.getId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }
        boolean isOnline = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);
        return changeToDto(user, isOnline);
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        boolean isOnline = userStatusRepository.findByUserId(user.get().getId())
                .map(UserStatus::isOnline)
                .orElse(false);
        return changeToDto(user.orElse(null), isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> changeToDto(user, userStatusRepository.findByUserId(user.getId())
                        .map(UserStatus::isOnline)
                        .orElse(false)))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserUpdateRequest request) {
        Optional<User> user = userRepository.findByUserId(request.userId());
        if (!user.get().getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (!user.get().getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (request.profileImage() != null) {
            Optional<BinaryContent> findBinaryContent = binaryContentRepository.findProfileByUserId(user.get().getId());
            findBinaryContent.ifPresent(binaryContent -> binaryContentRepository.deleteByContentId(binaryContent.getId()));
            BinaryContent binaryContent = new BinaryContent(user.get().getId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }

        user.get().update(request.username(), request.email(), request.phoneNumber(), request.password());
        boolean isOnline = userStatusRepository.findByUserId(user.get().getId())
                .map(UserStatus::isOnline)
                .orElse(false);
        return changeToDto(user.orElse(null), isOnline);
    }


    @Override
    public void delete(UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private UserDto changeToDto(User user, boolean isOnline) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), isOnline, user.getCreatedAt(), user.getUpdatedAt());
    }
}
