package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.BinaryContent.BinaryContentNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserAlreadyExistException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.jwt.JwtService;
import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.UserService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final BinaryContentService binaryContentService;
    private final BinaryContentRepository binaryContentRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @CacheEvict(cacheNames = "users", key = "'all'")    // 캐시 무효화
    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        BinaryContentCreateRequest binaryContentCreateRequest) {
        String email = userCreateRequest.email();
        String username = userCreateRequest.username();
        String password = passwordEncoder.encode(userCreateRequest.password());

        if (userRepository.existsByEmail(email)) {
            log.warn("[Creating User Failed: Email {} already exists]", email);
            throw new UserAlreadyExistException(Map.of("email", email));
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("[Creating User Failed: Username {} already exists]", username);
            throw new UserAlreadyExistException(Map.of("username", username));
        }
        // User 생성, 저장
        User user = userRepository.save(new User(username, email, password));
        // 프로필 이미지 저장
        BinaryContent binaryContent = null;
        if (binaryContentCreateRequest != null) {
            BinaryContentDto binaryContentDto = binaryContentService.create(
                binaryContentCreateRequest);
            binaryContent = binaryContentRepository.findById(binaryContentDto.getId())
                .orElseThrow(
                    () -> new BinaryContentNotFoundException(
                        Map.of("binaryContentId", binaryContentDto.getId())));
        }
        user.setProfile(binaryContent);

        return userMapper.toDto(user);
    }


    @Override
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
        return userMapper.toDto(user);
    }

    // 유저 목록 조회 결과 캐싱
    @Cacheable(
        cacheNames = "users",
        key = "'all'"
    )
    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        /* JwtService에서 온라인 상태인(JwtSession 정보가 저장되어 있고, 아직 만료되지 않은)
         * 사용자들의 id 모음
         */
        Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
            .map(JwtSession::getUserId)
            .collect(Collectors.toSet());

        List<User> users = userRepository.findAllFetchJoin();
        return users.stream()
            .sorted(Comparator.comparing(user -> user.getCreatedAt()))
            .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
            .toList();
    }

    @CacheEvict(cacheNames = "users", key = "'all'")
    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        BinaryContentCreateRequest binaryContentCreateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("[Updating User Failed: User with id {} not found]", userId);
            return new UserNotFoundException(Map.of("userId", userId));
        });

        String email = userUpdateRequest.newEmail();
        String username = userUpdateRequest.newUsername();
        String password = (userUpdateRequest.newPassword() != null) ? passwordEncoder.encode(
            userUpdateRequest.newPassword()) : null;

        if (userRepository.existsByEmail(email)) {
            log.warn("[Updating User Failed: Email {} already exists]", email);
            throw new UserAlreadyExistException(Map.of("email", email));
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("[Updating User Failed: Username {} already exists]", username);
            throw new UserAlreadyExistException(Map.of("username", username));
        }
        // 프로필 이미지 저장
        // 기존 프로필은 cascade로 자동 삭제
        BinaryContentDto binaryContentDto =
            (binaryContentCreateRequest != null) ? binaryContentService.create(
                binaryContentCreateRequest) : null;
        BinaryContent binaryContent =
            (binaryContentDto == null) ? null
                : binaryContentRepository.findById(binaryContentDto.getId())
                    .orElse(null);
        user.update(username, email, password, binaryContent);
        return userMapper.toDto(user);
    }

    @CacheEvict(cacheNames = "users", key = "'all'")
    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("[Updating User Failed: User with id {} not found]", userId);
            return new UserNotFoundException(Map.of("userId", userId));
        });
        userRepository.delete(user);
    }

}
