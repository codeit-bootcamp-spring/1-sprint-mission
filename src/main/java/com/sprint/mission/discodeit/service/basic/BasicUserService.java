package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ValidateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateUser validateUser;

    @Override
    public UserDto create(UserCreateRequest userRequest, Optional<BinaryContentCreateRequest> profileRequest) {
        // validateUser.validateUser(request.username(), request.email(), request.phoneNumber(), request.password());

        String username = userRequest.username();
        String email = userRequest.email();
        String phoneNumber = userRequest.phoneNumber();
        String password = userRequest.password();

        UUID nullableProfileId = profileRequest
                .map(request -> {
                    String fileName = request.fileName();
                    String contentType = request.contentType();
                    byte[] bytes = request.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        User user = new User(username, email, phoneNumber, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);
        Boolean isOnline = userStatusRepository.save(userStatus).isOnline();

        // Dto로 반환
        return changeToDto(createdUser, isOnline);
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Boolean isOnline = userStatusRepository.findByUserId(userId)
                .map(UserStatus::isOnline)
                .orElseThrow(() -> new ResourceNotFoundException("User status not found."));
        return changeToDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Boolean isOnline = userStatusRepository.findByUserId(user.getId())
                            .map(UserStatus::isOnline)
                            .orElseThrow(() -> new ResourceNotFoundException("User status not found."));
                    return changeToDto(user, isOnline);
                })
                .toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest userRequest, Optional<BinaryContentCreateRequest> profileRequest) {
        // validateUser.validateUser(request.username(), request.email(), request.phoneNumber(), request.password());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        String newUsername = userRequest.newUsername();
        String newEmail = userRequest.newEmail();
        String newPhoneNumber = userRequest.newPhoneNumber();
        String newPassword = userRequest.newPassword();

        UUID nullableProfileId = profileRequest
                .map(request -> {
                    if (user.getProfileId() != null){
                        binaryContentRepository.deleteById(user.getProfileId());
                    }

                    String fileName = request.fileName();
                    String contentType = request.contentType();
                    byte[] bytes = request.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        user.update(newUsername, newEmail, newPhoneNumber, newPassword, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Status not found."));
        userStatus.update(now);
        Boolean isOnline = userStatusRepository.save(userStatus).isOnline();

        // Dto로 반환
        return changeToDto(createdUser, isOnline);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        if (user.getProfileId() != null){
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    private UserDto changeToDto(User user, boolean isOnline) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfileId(),
                isOnline,
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
