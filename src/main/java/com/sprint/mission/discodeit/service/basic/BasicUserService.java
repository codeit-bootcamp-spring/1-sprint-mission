package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
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
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserCreateRequest userRequest, Optional<BinaryContentCreateRequest> profileRequest) {
        String username = userRequest.username();
        String email = userRequest.email();
        String phoneNumber = userRequest.phoneNumber();
        String password = userRequest.password();

        userValidator.validateUser(username, phoneNumber, email, password);
        userValidator.validateDuplicateUser(username, phoneNumber, email);

        UUID nullableProfileId = profileRequest.map(request -> {
            String fileName = request.fileName();
            String contentType = request.contentType();
            byte[] bytes = request.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);

        User user = new User(username, email, phoneNumber, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);
        Boolean isOnline = userStatusRepository.save(userStatus).isOnline();

        // Dto로 반환
        return userMapper.userEntityToDto(createdUser, isOnline);
    }

    @Override
    public UserDto findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User Status not found: " + user.getId()));
        Boolean isOnline = userStatus.isOnline();
        return userMapper.userEntityToDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(user -> {
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User Status not found: " + user.getId()));
            Boolean isOnline = userStatus.isOnline();
            return userMapper.userEntityToDto(user, isOnline);
        }).toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest userRequest, Optional<BinaryContentCreateRequest> profileRequest) {
        String newUsername = userRequest.newUsername();
        String newEmail = userRequest.newEmail();
        String newPhoneNumber = userRequest.newPhoneNumber();
        String newPassword = userRequest.newPassword();

        userValidator.validateUser(newUsername, newEmail, newPhoneNumber, newPassword);
        userValidator.validateDuplicateUser(newUsername, newPhoneNumber, newEmail);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        UUID nullableProfileId = profileRequest.map(request -> {
            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());
            }

            String fileName = request.fileName();
            String contentType = request.contentType();
            byte[] bytes = request.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);

        user.update(newUsername, newEmail, newPhoneNumber, newPassword, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User Status not found: " + user.getId()));
        userStatus.update(now);
        Boolean isOnline = userStatusRepository.save(userStatus).isOnline();

        // Dto로 반환
        return userMapper.userEntityToDto(createdUser, isOnline);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:" + userId));
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
