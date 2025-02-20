package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User createUser(UserCreate userCreateDTO, Optional<BinaryContentCreateDto> profileCreateDto) {
        String username = userCreateDTO.username();
        String userEmail = userCreateDTO.userEmail();
        if(userRepository.existsByEmail(userEmail)) {
            throw new IllegalArgumentException("이미 가입되어있는 이메일입니다." + userEmail + "다른 이메일을 사용해주세요");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(("이미 사용중인 닉네임입니다." + username + "다른 이름을 사용해주세요!"));
        }

        UUID nullableProfileId = profileCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);
        String password = userCreateDTO.password();

        User user = new User(username, userEmail, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getUserId(), now);
        userStatusRepository.save(userStatus);

        return createdUser;
    }

    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException(userId + ": 해당 유저를 찾을 수 없습니다."));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UserUpdate userUpdate, Optional<BinaryContentCreateDto> profileCreateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException( userId + " : 찾을 수 없는 유저입니다."));

        String newUsername = userUpdate.newUsername();
        String newEmail = userUpdate.newUserEmail();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("이미 가입되어있는 이메일 입니다." + newEmail);
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("사용중인 닉네임입니다" + newUsername + "다른 이름을 사용해주세요");
        }

        UUID nullableProfileId = profileCreateDto
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(binaryContentRepository::deleteById);

                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPassword = userUpdate.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfileId);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + "를 찾을 수 없습니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }
    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getUserId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getUserId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getUserEmail(),
                user.getProfileId(),
                online
        );
    }
}
