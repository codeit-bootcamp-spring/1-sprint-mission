package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
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
        userStatusService.create(user.getId());

        return user;
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.find(userId);
        validateUserExists(user);
        BinaryContent binaryContent = binaryContentService.find(user.getBinaryContentId());
        OnlineStatus onlineStatus = userStatusService.getOnlineStatus(userId);

        return UserResponseDto.from(user, binaryContent, onlineStatus);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserResponseDto.from(user,
                        binaryContentService.find(user.getBinaryContentId()),
                        userStatusService.getOnlineStatus(user.getId())))
                .toList();
    }
    @Override
    public void update(UserUpdateRequestDto userUpdateRequestDto, BinaryContentRequestDto binaryContentRequestDto) {
        validator.validate(userUpdateRequestDto.name(), userUpdateRequestDto.email());
        User user = userRepository.find(userUpdateRequestDto.userId());
        validateUserExists(user);
        
        if (binaryContentRequestDto != null) {
            BinaryContent binaryContent = binaryContentService.create(binaryContentRequestDto);
            user.updateBinaryContentId(binaryContent.getId());
        }
        user.update(userUpdateRequestDto.name(), userUpdateRequestDto.email(), userUpdateRequestDto.password());
        userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.find(userId);
        validateUserExists(user);
        userRepository.delete(user);
    }

    @Override
    public void validateUserExists(User user) {
        if (user == null) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
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
