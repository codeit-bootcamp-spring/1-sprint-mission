package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        user = userRepository.save(user);

        UserStatus userStatus = new UserStatus(user, Instant.now());
        userStatusRepository.save(userStatus);

        if(request.getProfileImage() != null && !request.getProfileImage().isEmpty()){
            saveProfileImage(user, request.getProfileImage());
        }

        return new UserResponse(user, userStatus.isOnline());
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        UserStatus userStatus = userStatusRepository.findByUser(user).orElse(new UserStatus(user, Instant.MIN));
        return new UserResponse(user, userStatus.isOnline());
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUser(user)
                            .orElse(new UserStatus(user, Instant.MIN));
                    return new UserResponse(user, userStatus.isOnline());
                })
                .toList();
    }

    @Override
    public UserResponse update(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.update(request.getUsername(), request.getEmail(), request.getPassword());
        user = userRepository.save(user);

        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            saveProfileImage(user, request.getProfileImage());
        }

        UserStatus userStatus = userStatusRepository.findByUser(user)
                .orElse(new UserStatus(user, Instant.MIN));

        return new UserResponse(user, userStatus.isOnline());
    }


    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userStatusRepository.deleteByUser(user);

        binaryContentRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    private void saveProfileImage(User user, MultipartFile profileImage) {
        try {
            BinaryContent binaryContent = new BinaryContent(user, null, profileImage.getBytes(), profileImage.getContentType());
            binaryContentRepository.save(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save profile image", e);
        }
    }
}
