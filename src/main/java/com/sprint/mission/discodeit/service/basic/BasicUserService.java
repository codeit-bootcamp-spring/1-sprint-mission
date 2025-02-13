package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(UserDto userDto) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getName().equals(userDto.getName())
                    || user.getEmail().equals(userDto.getEmail())) {
                throw new DuplicateException("중복된 이름 혹은 이메일 입니다.");
            }
        }

        String password = generatePassword(userDto.getPassword());
        User newUser = User.of(userDto.getName(), userDto.getEmail(), password);

        userStatusRepository.save(UserStatus.from(newUser.getId()));
        return userRepository.save(newUser);
    }

    @Override
    public UserInfoDto readUser(UUID userId) {
        User user = userRepository.findById(userId);
        UserStatus userStatus = userStatusRepository.findById(user.getId());
        return UserInfoDto.of(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), userStatus);
    }

    @Override
    public List<UserInfoDto> readAll() {
        List<User> users = userRepository.findAll();
        List<UserInfoDto> userInfoDtos = new ArrayList<>(100);
        for (User user : users) {
            UserStatus userStatus = userStatusRepository.findById(user.getId());
            UserInfoDto userInfoDto = UserInfoDto.of(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), userStatus);
            userInfoDtos.add(userInfoDto);
        }
        return userInfoDtos;
    }

    @Override
    public void updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findById(userId);
        user.updateName(userDto.getName());
        user.updateEmail(userDto.getEmail());
        user.updatePassword(generatePassword(userDto.getPassword()));

        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByUserId(userId);
        if (binaryContent.isPresent()) {
            BinaryContent content = binaryContent.get();
            FileManager fileManager = new FileManager("files");
            fileManager.deleteFile(content.getName());
        }
        userStatusRepository.delete(userId);
        userRepository.deleteUser(userId);
    }

    private String generatePassword(String password) {
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());

            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("비밀번호 암호화 오류");
        }
        return builder.toString();
    }
}