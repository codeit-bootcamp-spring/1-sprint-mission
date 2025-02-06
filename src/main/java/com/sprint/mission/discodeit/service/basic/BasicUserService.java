package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.UserUtil.validUserId;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public User create(UserCreateRequest request) {
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

        return user;
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        User user = userRepository.findByUserId(userId);
        boolean isOnline = userStatusRepository.findByUserId(user.getId()).isOnline();
        return changeToDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), userStatusRepository.findByUserId(user.getId()).isOnline()))
                .collect(Collectors.toList());
    }

    @Override
    public User update(UserUpdateRequest request) {
        User user = userRepository.findByUserId(request.userId());
        if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (request.profileImage() != null) {
            binaryContentRepository.deleteByUserId(user.getId());
            BinaryContent binaryContent = new BinaryContent(user.getId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }

        user.update(request.username(), request.email(), request.phoneNumber(), request.password());
        return userRepository.save(user);
    }


    @Override
    public void delete(UUID userId) {
        User user = userRepository.findByUserId(userId);
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private UserDto changeToDto(User user, boolean isOnline) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), isOnline);
    }
}
