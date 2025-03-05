package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserReadResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service("basicUserService")
@Primary
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserReadResponse create(UserCreateRequest userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent() ||
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        UUID profileImageId = Optional.ofNullable(userDTO.getProfileImageId())
                .map(UUID::fromString)
                .orElse(null);

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User(UUID.randomUUID(), userDTO.getUsername(), userDTO.getEmail(), profileImageId, encodedPassword);
        userRepository.save(user);

        log.info("✅ 사용자 생성 완료: {}", user.getId());

        return new UserReadResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageId(),
                user.getLastActive(),
                false
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserReadResponse> read(UUID id) {
        return userRepository.findById(id)
                .map(user -> new UserReadResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getProfileImageId(),
                        user.getLastActive(),
                        false
                ));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserReadResponse> readAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserReadResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getProfileImageId(),
                        user.getLastActive(),
                        false
                ))
                .toList();
    }

    @Transactional
    @Override
    public void update(UUID id, UserUpdateRequest userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));

        user.setUsername(userDTO.getNewUsername());
        user.setEmail(userDTO.getNewEmail());
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        userRepository.save(user);
        log.info("✅ 사용자 업데이트 완료: {}", id);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id);
        }
        userRepository.deleteById(id);
        log.info("✅ 사용자 삭제 완료: {}", id);
    }

    @Transactional
    @Override
    public boolean updateLastSeen(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            user.setLastActive(Instant.now());
            user.setOnline(true);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    @Transactional
    @Override
    public void updateProfileImage(UUID userId, UUID imageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        user.setProfileImageId(imageId);
        userRepository.save(user);
        log.info("✅ 프로필 이미지 업데이트 완료: {}", imageId);
    }
}
