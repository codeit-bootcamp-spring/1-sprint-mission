package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentService binaryContentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtSessionRepository jwtSessionRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest.Create request, MultipartFile userProfileImage) {

        UUID requestId = UUID.fromString(MDC.get("requestId"));

        checkDuplicateEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = userRepository.save(User.createUserWithoutProfile(
            request.getUsername(), request.getEmail(), encodedPassword));

        binaryContentService.save(userProfileImage, newUser.getId(),
            requestId);

        log.info("Created user - id: {}", newUser.getId());
        return userMapper.entityToDto(newUser, false);
    }

    @Override
    public List<UserResponse> findAll() {

        Set<UUID> onlineUserIds = jwtSessionRepository.findUserIdsWithActiveAccessTokens(
            Instant.now());

        List<UserResponse> userResponses = userRepository.findAll().stream()
            .map(user -> userMapper.entityToDto(user, onlineUserIds.contains(user.getId())))
            .collect(Collectors.toList());

        return userResponses;
    }

    @Override
    public UserResponse findById(UUID id) {
        return userMapper.entityToDto(findByIdOrThrow(id));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userMapper.entityToDto(findByUsernameOrThrow(username));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UserRequest.Update request,
        MultipartFile userProfileImage) {

        UUID requestId = UUID.fromString(MDC.get("requestId"));
        User user = findByIdOrThrow(id);

        Optional.ofNullable(request.getNewUsername()).ifPresent(user::updateName);
        Optional.ofNullable(request.getNewPassword()).ifPresent(user::updatePassword);

        Optional.ofNullable(request.getNewEmail())
            .ifPresent(email -> {
                checkDuplicateEmail(email);
                user.updateEmail(email);
            });

        binaryContentService.save(userProfileImage, user.getId(), requestId);

        log.info("Updated user - id: {}", user.getId());
        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        userRepository.deleteById(id);
        notificationRepository.deleteAllByReceiverId(id);
        log.info("Deleted user - id: {}", id);
    }

    private User findByIdOrThrow(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("id", id)));
    }

    private User findByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,
                    Map.of("Username", username)));
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXIST,
                Map.of("email", email));
        }
    }
}
