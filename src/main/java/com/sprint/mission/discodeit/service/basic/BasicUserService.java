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
import java.util.stream.Collectors;

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

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        UUID profileImageId = null;
        if (userDTO.getProfileImageId() != null && !userDTO.getProfileImageId().isEmpty()) {
            profileImageId = UUID.fromString(userDTO.getProfileImageId());
        }

        User user = new User(UUID.randomUUID(), userDTO.getUsername(), userDTO.getEmail(), profileImageId, encodedPassword);
        userRepository.save(user);

        System.out.println("✅ 사용자 생성 완료: " + user.getId());

        return new UserReadResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageId(),
                user.getLastActive(),
                false
        );
    }

    @Override
    public Optional<UserReadResponse> read(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            System.out.println("❌ 사용자 조회 실패 (존재하지 않음): " + id);
        } else {
            System.out.println("✅ 사용자 조회 성공: " + userOptional.get().getId());
        }

        return userOptional.map(user ->
                new UserReadResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getProfileImageId(),
                        user.getLastActive(),
                        false
                )
        );
    }

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
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void update(UUID id, UserUpdateRequest userDTO) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            System.out.println("✅ 사용자 업데이트 시작: " + id);

            // 수정: 새로운 필드명을 사용하여 업데이트
            user.setUsername(userDTO.getNewUsername());
            user.setEmail(userDTO.getNewEmail());
            if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            }

            userRepository.save(user);
            System.out.println("✅ 사용자 업데이트 완료: " + user.getId());

        }, () -> {
            System.out.println("❌ 사용자 업데이트 실패 (사용자 없음): " + id);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id);
        });
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        System.out.println("🗑 사용자 삭제 요청: " + id);
        userRepository.deleteById(id);
        System.out.println("✅ 사용자 삭제 완료: " + id);
    }

    @Transactional
    @Override
    public boolean updateLastSeen(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            user.setLastActive(Instant.now());
            user.setOnline(true);
            userRepository.save(user);
            System.out.println("✅ 마지막 활동 시간 업데이트 완료: " + userId);
            return true;
        }).orElse(false);
    }

    @Transactional
    @Override
    public void updateProfileImage(UUID userId, UUID imageId) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            user.setProfileImageId(imageId);
            userRepository.save(user);
            System.out.println("✅ 프로필 이미지 업데이트 완료: " + imageId);
        }, () -> {
            System.out.println("❌ 사용자 ID를 찾을 수 없음: " + userId);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        });
    }
}
