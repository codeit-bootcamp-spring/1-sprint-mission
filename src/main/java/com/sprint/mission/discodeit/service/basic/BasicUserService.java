package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.LoginStatusChecker;
import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final UserValidator userValidator;

    private final MultipartFileConverter multipartFileConverter;
    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final LoginStatusChecker loginStatusChecker;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile) {
        // name 중복 여부
        userValidator.validateDuplicateByName(createUserRequest.username());
        // email 중복 여부
        userValidator.validateDuplicateUserByEmail(createUserRequest.email());

        // 비밀번호 암호화
        String hashedPassword = bCryptPasswordEncoder.encode(createUserRequest.password());
        User user = userMapper.toEntity(createUserRequest, hashedPassword);

        // 이미지는 선택적으로 등록
        updateProfileImage(user, profileImageFile);
        User savedUser = userRepository.saveUser(user);

        return userMapper.toUserDto(savedUser, false);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserByIdOrThrow(UUID userId) {
        User foundUser = userValidator.validateUserExistsByUserId(userId);

        return userMapper.toUserDto(foundUser, loginStatusChecker.getOnline(foundUser));
    }

    @Cacheable(cacheNames = "users", sync = true)
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAllUsers().stream()
            .map(user -> findUserByIdOrThrow(user.getId()))
            .collect(Collectors.toList());
    }

    //    @CacheEvict(cacheNames = "users", allEntries = true)
    @Override
    @Transactional
    public UserDto updateUser(UUID userId, UpdateUserRequest updateUserRequest,
        MultipartFile profileImageFile) {
        User foundUser = userValidator.validateUserExistsByUserId(userId);

        foundUser.updateUserInfo(updateUserRequest.newUsername(), updateUserRequest.newEmail(),
            updateUserRequest.newPassword());

        updateProfileImage(foundUser, profileImageFile);

        return userMapper.toUserDto(foundUser, loginStatusChecker.getOnline(foundUser));
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        // 유저 존재 여부 확인
        userValidator.validateUserExistsByUserId(userId);

        // cascade 옵션으로 userStatus, binaryContent 자동 삭제

        // 유저 삭제
        userRepository.removeUser(userId);
    }

    private void updateProfileImage(User user, MultipartFile profileImageFile) {
        Optional.ofNullable(profileImageFile)
            .ifPresent(file -> {
                BinaryContent binaryContent = binaryContentRepository.saveBinaryContent(
                    BinaryContent.of(file.getOriginalFilename(), file.getSize(),
                        file.getContentType()));

                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            CompletableFuture<UUID> uuidCompletableFuture =
                                null;
                            try {
                                uuidCompletableFuture = binaryContentStorage.putAsync(
                                    binaryContent.getId(),
                                    multipartFileConverter.toByteArray(file));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            uuidCompletableFuture.whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    binaryContent.updateBinaryContentUploadStatus(
                                        BinaryContentUploadStatus.FAILED);
                                } else {
                                    binaryContent.updateBinaryContentUploadStatus(
                                        BinaryContentUploadStatus.SUCCESS);
                                }
                                binaryContentRepository.saveBinaryContent(binaryContent);
                            });
                        }
                    }
                );

                user.updateProfileImage(binaryContent);
            });
    }
}
