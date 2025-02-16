package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    // 사용자 생성 (프로필 이미지 포함 가능, username/email 중복 검사, UserStatus 생성)
    public User createUser(UserCreateDto dto) {
        if (userRepository.loadAllUser().stream()
                .anyMatch(user -> user.getUsername().equals(dto.getUsername()) || user.getEmail().equals(dto.getEmail()))) {
            throw new IllegalArgumentException("Username or Email already exists.");
        }

        User user = new User(dto.getUsername(), dto.getPassword(), dto.getEmail());
        userRepository.saveUser(user);

        UserStatus status = new UserStatus(user.getId());
        userStatusRepository.save(status);

        if (dto.getProfileImage() != null) {
            BinaryContent profile = new BinaryContent(user.getId(), null, "profile.jpg", "image/jpeg", dto.getProfileImage());
            binaryContentRepository.save(profile);
        }

        return user;
    }

    // 사용자 정보 수정 (선택적 프로필 이미지 교체 포함)
    public User updateUser(UserUpdateDto dto) {
        User user = userRepository.loadAllUser().stream()
                .filter(u -> u.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (dto.getUsername() != null) {
            user.updateName(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            user.updateEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.updatePassword(dto.getPassword());
        }

        userRepository.saveUser(user);

        if (dto.getProfileImage() != null) {
            binaryContentRepository.findById(user.getId())
                    .ifPresent(content -> binaryContentRepository.deleteById(user.getId()));
            BinaryContent profile = new BinaryContent(user.getId(), null, "profile.jpg", "image/jpeg", dto.getProfileImage());
            binaryContentRepository.save(profile);
        }

        return user;
    }

    // 사용자 단건 조회 (DTO 활용, 패스워드 제외, 온라인 상태 포함)
    public UserResponseDto findUserById(UUID userId) {
        User user = userRepository.loadAllUser().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        UserStatus status = userStatusRepository.findByUserId(userId).orElse(null);

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(),
                status != null && status.isActiveNow() ? "ONLINE" : "OFFLINE");
    }

    // 전체 사용자 조회 (DTO 활용, 패스워드 제외, 온라인 상태 포함)
    public List<UserResponseDto> findAllUsers() {
        return userRepository.loadAllUser().stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getId()).orElse(null);
                    return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(),
                            status != null && status.isActiveNow() ? "ONLINE" : "OFFLINE");
                })
                .collect(Collectors.toList());
    }

    // 사용자 삭제 (연관 도메인 삭제 포함)
    public void deleteUser(UUID userId) {
        User user = userRepository.loadAllUser().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        binaryContentRepository.findById(userId).ifPresent(content -> binaryContentRepository.deleteById(userId)); // 프로필 이미지 삭제
        userStatusRepository.findByUserId(userId).ifPresent(status -> userStatusRepository.deleteById(status.getId())); // 상태 정보 삭제
        userRepository.deleteUser(user); // 사용자 삭제
    }
}
