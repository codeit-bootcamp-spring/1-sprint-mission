package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
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
    private final FileManager fileManager;


    @Override
    public User createUser(UserRequest userRequest) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getName().equals(userRequest.name())
                    || user.getEmail().equals(userRequest.email())) {
                throw new DuplicateException("중복된 이름 혹은 이메일 입니다.");
            }
        }

        String password = generatePassword(userRequest.password());
        User newUser = User.of(userRequest.name(), userRequest.email(), password);

        userStatusRepository.save(UserStatus.from(newUser.getId()));
        return userRepository.save(newUser);
    }

    @Override
    public UserDetailResponse readUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
        UserStatus userStatus = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + userId));
        return UserDetailResponse.of(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), userStatus.isOnline());
    }

    @Override
    public List<UserDetailResponse> readAll() {
        List<User> users = userRepository.findAll();
        List<UserDetailResponse> userDetailResponses = new ArrayList<>(100);
        for (User user : users) {
            UserStatus userStatus = userStatusRepository.findById(user.getId())
                    .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + user.getId()));
            UserDetailResponse userDetailResponse = UserDetailResponse.of(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), userStatus.isOnline());
            userDetailResponses.add(userDetailResponse);
        }
        return userDetailResponses;
    }

    @Override
    public void updateUser(UUID userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
        user.updateName(userRequest.name());
        user.updateEmail(userRequest.email());
        user.updatePassword(generatePassword(userRequest.password()));

        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByUserId(userId);
        if (binaryContent.isPresent()) {
            BinaryContent content = binaryContent.get();
            fileManager.deleteFile(Path.of(content.getPath()));
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