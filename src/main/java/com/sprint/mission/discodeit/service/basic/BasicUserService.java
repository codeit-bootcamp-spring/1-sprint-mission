package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.FileConversionException;
import com.sprint.mission.discodeit.global.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;
    private final JwtSessionRepository jwtSessionRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest.Create request, MultipartFile userProfileImage) {

        checkDuplicateEmail(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        BinaryContent newProfile = null;
        if (userProfileImage != null && !userProfileImage.isEmpty()) {
            newProfile = binaryContentRepository.save(BinaryContent.createBinaryContent(
                userProfileImage.getOriginalFilename(),
                userProfileImage.getSize(),
                userProfileImage.getContentType()));
            binaryContentStorage.put(newProfile.getId(), convertToBytes(userProfileImage));
        }

        User newUser = userRepository.save(User.createUser(
            request.getUsername(), request.getEmail(), encodedPassword, newProfile));

        log.info("Created user - id: {}", newUser.getId());
        return userMapper.entityToDto(newUser, false);
    }

    @Override
    public List<UserResponse> findAll() {

        Set<UUID> onlineUserIds = jwtSessionRepository.findUserIdsWithActiveAccessTokens();

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

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Override
    @Transactional
    public UserResponse update(UUID id, UserRequest.Update request,
        MultipartFile userProfileImage) {
        User user = findByIdOrThrow(id);

        Optional.ofNullable(request.getNewUsername()).ifPresent(user::updateName);
        Optional.ofNullable(request.getNewPassword()).ifPresent(user::updatePassword);

        Optional.ofNullable(request.getNewEmail())
            .ifPresent(email -> {
                checkDuplicateEmail(email);
                user.updateEmail(email);
            });

        Optional.ofNullable(userProfileImage)
            .ifPresent(profile -> {
                if (!profile.isEmpty()) { // 파라미터는 있는데, 파일이 안 들어올 때
                    BinaryContent binaryContent = binaryContentRepository.save(
                        BinaryContent.createBinaryContent(
                            profile.getOriginalFilename(),
                            profile.getSize(),
                            profile.getContentType()));
                    binaryContentStorage.put(binaryContent.getId(), convertToBytes(profile));
                    user.updateProfile(binaryContent);
                }
            });

        log.info("Updated user - id: {}", user.getId());
        return userMapper.entityToDto(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        userRepository.deleteById(id);
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

    private byte[] convertToBytes(MultipartFile imageFile) {
        try {
            return imageFile.getBytes();
        } catch (IOException e) {
            throw new FileConversionException(ErrorCode.INTERNAL_SERVER_ERROR,
                Map.of("fileName", imageFile.getOriginalFilename()));
        }
    }

}
