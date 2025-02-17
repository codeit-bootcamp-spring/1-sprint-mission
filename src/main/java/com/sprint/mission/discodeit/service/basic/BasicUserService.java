package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.dto.userService.UserProfileImageRequest;
import com.sprint.mission.discodeit.dto.userService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Qualifier("basicUserService")
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public User create(UserCreateRequest userRequest, UserProfileImageRequest profileImage) {
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new IllegalArgumentException("Username already exists: " + userRequest.username());
        }
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new IllegalArgumentException("Email already exists: " + userRequest.email());
        }
        User user = new User(userRequest.username(), userRequest.email(), userRequest.password());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        if(profileImage != null && profileImage.data() != null) {
            BinaryContent profile = new BinaryContent(user.getId(),null, profileImage.contentType(), profileImage.fileName(), profileImage.data());
            binaryContentRepository.save(profile);
        }
        return user;

    }

    @Override
    public UserDTO find(UUID userId) {
         User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        // UserStatus 가져오기
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElse(null);

        //DTO 변환 후 반환
        return UserDTO.from(user, userStatus);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);
                    return UserDTO.from(user, userStatus);
                })
                .toList();

    }

    @Override
    public UserDTO update(UserUpdateRequest userRequest, UserProfileImageRequest profileImage) {
        User user = userRepository.findById(userRequest.userId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + userRequest.userId() + " not found"));

        // 사용자 정보 업데이트
        user.update(userRequest.newUsername(), userRequest.newEmail(), userRequest.newPassword());

        if (profileImage != null && profileImage.data() != null) {
            BinaryContent profile = new BinaryContent(
                    user.getId(),
                    null,
                    profileImage.contentType(),
                    profileImage.fileName(),
                    profileImage.data()
            );
            binaryContentRepository.save(profile);
        }

        //  반환값 추가 (UserDTO로 변환하여 반환)
        return UserDTO.from(user, userStatusRepository.findByUserId(user.getId()).orElse(null));
    }


    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }
}
