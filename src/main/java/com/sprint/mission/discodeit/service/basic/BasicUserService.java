package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserValidator validator;

    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @Override
    public User create(UserCreateRequest userRequest, Optional<BinaryContentRequest> binaryContentRequest) {
        validator.validate(userRequest.username(), userRequest.email());
        validateDuplicateName(userRequest.username());
        validateDuplicateEmail(userRequest.email());

        BinaryContent profile = binaryContentRequest
                .map(binaryContentService::create)
                .orElse(null);
        User user = userRepository.save(new User(userRequest.username(), userRequest.email(), userRequest.password(), profile));
        userStatusService.create(UserStatusCreateRequest.from(user.getId(), Instant.now()));

        return user;
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        return getUserInfo(user);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::getUserInfo)
                .toList();
    }

    @Override
    public UserResponse getUserInfo(User user) {
        UUID binaryContentId = null;
        if (user.getProfile() != null) {
            BinaryContent binaryContent = binaryContentService.find(user.getProfile().getId());
            binaryContentId = binaryContent.getId();
        }
        Boolean online = userStatusService.getOnlineStatus(user.getId());

        return UserResponse.from(user, binaryContentId, online);
    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentRequest> binaryContentRequest) {
        validator.checkEmailFormat(userUpdateRequest.newEmail());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));
        validateDuplicateName(userUpdateRequest.newUsername());
        validateDuplicateEmail(userUpdateRequest.newEmail());

        BinaryContent profile = binaryContentRequest
                .map(binaryContentService::create)
                .orElse(null);
        user.update(profile, userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword());

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void validateDuplicateName(String name) {
        userRepository.findAll().forEach(user -> user.validateDuplicateName(name));
    }

    @Override
    public void validateDuplicateEmail(String email) {
        userRepository.findAll().forEach(user -> user.validateDuplicateEmail(email));
    }
}
