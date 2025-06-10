package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.CacheConfig;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AsyncUploadService;
import com.sprint.mission.discodeit.service.UserService;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AsyncUploadService asyncUploadService;


    @Timed(value = "user.create.async", description = "Time taken for creation with async upload")
    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.debug("사용자 생성 시작: {}", userCreateRequest);

        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.withEmail(email);
        }
        if (userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.withUsername(username);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {
                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);

                //트랜잭션 커밋 후 비동기 업로드 실행
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            asyncUploadService.uploadFileAsync(binaryContent.getId(), bytes);
                        }
                    }
                );

                return binaryContent;
            })
            .orElse(null);
        String password = userCreateRequest.password();

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword, nullableProfile);

        userRepository.save(user);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictAllUsersCache();
                    log.info("사용자 생성 완료: id={}, username={} - 트랜잭션 커밋 후 전체 사용자 목록 캐시 무효화",
                        user.getId(), username);
                }
            }
        );

        log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.USER_DETAIL, key = "#userId")
    @Override
    public UserDto find(UUID userId) {
        log.debug("사용자 조회 시작: id={}", userId);
        UserDto userDto = userRepository.findById(userId)
            .map(userMapper::toDto)
            .orElseThrow(() -> UserNotFoundException.withId(userId));
        log.info("사용자 조회 완료: id={}", userId);
        return userDto;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.ALL_USERS, key = "'all_users'")
    @Override
    public List<UserDto> findAll() {
        log.debug("모든 사용자 조회 시작");
        Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
            .map(JwtSession::getUserId)
            .collect(Collectors.toSet());

        List<UserDto> userDtos = userRepository.findAllWithProfile()
            .stream()
            .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
            .toList();
        log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());
        return userDtos;
    }

    @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
    @Timed(value = "user.update.async", description = "Time taken for update with async upload")
    @Transactional
    @CachePut(value = CacheConfig.USER_DETAIL, key = "#userId")
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.debug("사용자 수정 시작: id={}, request={}", userId, userUpdateRequest);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                UserNotFoundException exception = UserNotFoundException.withId(userId);
                return exception;
            });

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (userRepository.existsByEmail(newEmail)) {
            throw UserAlreadyExistsException.withEmail(newEmail);
        }

        if (userRepository.existsByUsername(newUsername)) {
            throw UserAlreadyExistsException.withUsername(newUsername);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {

                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);

                //트랜잭션 커밋 후 비동기 업로드 실행
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            asyncUploadService.uploadFileAsync(binaryContent.getId(), bytes);
                        }
                    }
                );

                return binaryContent;
            })
            .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        String hashedNewPassword = Optional.ofNullable(newPassword).map(passwordEncoder::encode)
            .orElse(null);
        user.update(newUsername, newEmail, hashedNewPassword, nullableProfile);

        UserDto updatedUserDto = userMapper.toDto(user);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictAllUsersCache();
                    log.info("사용자 수정 완료: id={} - 트랜잭션 커밋 후 사용자 상세 캐시 갱신, 전체 사용자 목록 캐시 무효화", userId);
                }
            }
        );
        return updatedUserDto;
    }

    @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
    @Transactional
    @Override
    public void delete(UUID userId) {
        log.debug("사용자 삭제 시작: id={}", userId);

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.withId(userId);
        }

        userRepository.deleteById(userId);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictUserRelatedCaches(userId);
                    log.info("사용자 삭제 완료: id={} - 트랜잭션 커밋 후 관련 캐시 무효화", userId);
                }
            }
        );
    }

    @CacheEvict(value = CacheConfig.ALL_USERS, allEntries = true)
    public void evictAllUsersCache() {
        log.debug("전체 사용자 목록 캐시 무효화");
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USER_DETAIL, key = "#userId"),
        @CacheEvict(value = CacheConfig.ALL_USERS, allEntries = true),
        @CacheEvict(value = CacheConfig.USER_CHANNELS, key = "#userId"),
        @CacheEvict(value = CacheConfig.USER_NOTIFICATIONS, key = "#userId")
    })
    public void evictUserRelatedCaches(UUID userId) {
        log.debug("사용자 관련 모든 캐시 무효화: userId={}", userId);
    }


}
