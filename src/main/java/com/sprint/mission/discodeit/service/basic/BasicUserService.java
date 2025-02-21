package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    public User create(UserCreateRequestDto userRequestDto, BinaryContentRequestDto binaryContentRequestDto) {
        validator.validate(userRequestDto.name(), userRequestDto.email());
        validateDuplicateName(userRequestDto.name());
        validateDuplicateEmail(userRequestDto.email());

        BinaryContent binaryContent = binaryContentService.create(binaryContentRequestDto);
        User user = userRepository.save(new User(binaryContent.getId(), userRequestDto.name(), userRequestDto.email(), userRequestDto.password()));
        userStatusService.create(UserStatusCreateDto.from(user.getId()));

        return user;
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = Optional.ofNullable(userRepository.find(userId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        return getUserInfo(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::getUserInfo)
                .toList();
    }

    @Override
    public UserResponseDto getUserInfo(User user) {
        BinaryContent binaryContent = binaryContentService.find(user.getBinaryContentId());
        OnlineStatus onlineStatus = userStatusService.getOnlineStatus(user.getId());

        return UserResponseDto.from(user, binaryContent, onlineStatus);
    }

    @Override
    public User update(UserUpdateRequestDto userUpdateRequestDto, BinaryContentRequestDto binaryContentRequestDto) {
        validator.validate(userUpdateRequestDto.name(), userUpdateRequestDto.email());
        User user = Optional.ofNullable(userRepository.find(userUpdateRequestDto.userId()))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        if (binaryContentRequestDto != null) {
            BinaryContent binaryContent = binaryContentService.create(binaryContentRequestDto);
            user.updateBinaryContentId(binaryContent.getId());
        }
        user.update(userUpdateRequestDto.name(), userUpdateRequestDto.email(), userUpdateRequestDto.password());
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
