package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserValidator validator;

    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @Override
    public User create(UserCreateRequest userRequest, BinaryContentRequest binaryContentRequest) {
        validator.validate(userRequest.name(), userRequest.email());
        validateDuplicateName(userRequest.name());
        validateDuplicateEmail(userRequest.email());

        BinaryContent binaryContent = binaryContentService.create(binaryContentRequest);
        User user = userRepository.save(new User(binaryContent.getId(), userRequest.name(), userRequest.email(), userRequest.password()));
        userStatusService.create(UserStatusCreate.from(user.getId()));

        return user;
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = Optional.ofNullable(userRepository.find(userId))
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
        BinaryContent binaryContent = binaryContentService.find(user.getBinaryContentId());
        OnlineStatus onlineStatus = userStatusService.getOnlineStatus(user.getId());

        return UserResponse.from(user, binaryContent, onlineStatus);
    }

    @Override
    public User update(UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest) {
        validator.validate(userUpdateRequest.name(), userUpdateRequest.email());
        User user = Optional.ofNullable(userRepository.find(userUpdateRequest.userId()))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        if (binaryContentRequest != null) {
            BinaryContent binaryContent = binaryContentService.create(binaryContentRequest);
            user.updateBinaryContentId(binaryContent.getId());
        }
        user.update(userUpdateRequest.name(), userUpdateRequest.email(), userUpdateRequest.password());
        userRepository.save(user);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        binaryContentService.delete(userRepository.find(userId).getBinaryContentId());
        userStatusService.deleteByUserId(userId);
        userRepository.delete(userId);
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
