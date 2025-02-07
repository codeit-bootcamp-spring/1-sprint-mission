package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto create(UserCreateRequest request) {
        // username, email 중복 검사
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists.");
        }
        // user 저장
        User user = new User(request.username(), request.email(), request.phoneNumber(), request.password());
        userRepository.save(user);
        // user 상태 저장
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);
        // profileImage가 있을 경우 binaryContent에 저장
        if (request.profileImage() != null) {
            BinaryContent binaryContent = new BinaryContent(user.getId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }
        // Dto로 반환
        return changeToDto(user, userStatus.isOnline());
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        // userId로 user 찾기
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        // userStatus 찾기
        boolean isOnline = userStatusRepository.findByUserId(user.getId()).map(UserStatus::isOnline).orElse(false);
        // Dto로 반환
        return changeToDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        // 모든 유저 조회
        List<User> users = userRepository.findAll();
        // Dto로 반환
        return users.stream().map(user -> changeToDto(user, userStatusRepository.findByUserId(user.getId()).map(UserStatus::isOnline).orElse(false))).collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserUpdateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists.");
        }
        User user = userRepository.findByUserId(request.userId()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        // user 업데이트
        user.update(request.username(), request.email(), request.phoneNumber(), request.password());
        if (request.profileImage() != null) {
            Optional<BinaryContent> findBinaryContent = binaryContentRepository.findProfileByUserId(user.getId());
            if (findBinaryContent.isPresent()) {
                binaryContentRepository.deleteByContentId(findBinaryContent.get().getId());
            }
            BinaryContent binaryContent = new BinaryContent(user.getId(), null, request.profileImage());
            binaryContentRepository.save(binaryContent);
        }

        boolean isOnline = userStatusRepository.findByUserId(user.getId()).map(UserStatus::isOnline).orElse(false);
        return changeToDto(user, isOnline);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private UserDto changeToDto(User user, boolean isOnline) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), isOnline, user.getCreatedAt(), user.getUpdatedAt());
    }
}
