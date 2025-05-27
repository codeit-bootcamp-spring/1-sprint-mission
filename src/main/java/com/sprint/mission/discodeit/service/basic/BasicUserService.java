package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        log.info("Processing user creation: username={}, email={}", username, email);
        log.debug("hasProfile={}", optionalProfileCreateRequest.isPresent());

        if (userRepository.existsByEmail(email)) {
            log.warn("User creation failed: email already exists - {}", email);
            throw UserExceptions.emailAlreadyExists(email);
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("User creation failed: username already exists - {}", username);
            throw UserExceptions.userNameAlreadyExists(username);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {
                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();

                log.debug("Generating user profile: filename={}, contentType={}, size={}",
                    fileName, contentType, bytes.length);
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);

        String password = userCreateRequest.password();

        String hashedPassword = passwordEncoder.encode(password);
        log.debug("Password hashed successfully for user: {}", username);

        User user = new User(username, email, hashedPassword, nullableProfile);
        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user, now);

        userRepository.save(user);

        log.info("User created successfully: userId={}, username={}", user.getId(), username);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        log.debug("Finding user by id: {}", userId);

        return userRepository.findById(userId)
            .map(user -> {
                log.debug("User found: userId={}, username={}", userId, user.getUsername());
                return userMapper.toDto(user);
            })
            .orElseThrow(() -> {
                log.warn("User not found: userId={}", userId);
                return UserExceptions.notFound(userId);
            });
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("Finding all user");
        return userRepository.findAllWithProfileAndStatus()
            .stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

        log.info("Processing user update: userId={}, newUsername={}, newEmail={}",
            userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User update failed: user not found - userId={}", userId);
                return UserExceptions.notFound(userId);
            });

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            log.warn("User update failed: email already exists - {}", newEmail);
            throw UserExceptions.emailAlreadyExists(newEmail);
        }
        if (userRepository.existsByUsername(newUsername)) {
            log.warn("User update failed: username already exists - {}", newUsername);
            throw UserExceptions.userNameAlreadyExists(newUsername);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {

                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();

                log.debug("Generating user new profile: filename={}, contentType={}, size={}",
                    fileName, contentType, bytes.length);
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        String hashedNewPassword = null;
        if (newPassword != null) {
            hashedNewPassword = passwordEncoder.encode(newPassword);
            log.debug("New password hashed successfully for userId: {}", userId);
        }

        user.update(newUsername, newEmail, hashedNewPassword, nullableProfile);

        log.info("User updated successfully: userId={}", userId);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.info("Processing user deletion: userId={}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User deletion failed: user not found - userId={}", userId);
            throw UserExceptions.notFound(userId);
        }

        userRepository.deleteById(userId);
        log.info("User deleted successfully: userId={}", userId);
    }
}
