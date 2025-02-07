package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserDto userDto) {
        String password = generatePassword(userDto.getPassword());
        User user = User.of(userDto.getName(), userDto.getLoginId(), password);
        return userRepository.save(user);
    }

    @Override
    public User readUser(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findById(userId);
        user.updateName(userDto.getName());
        user.updateLoginId(userDto.getLoginId());
        user.updatePassword(generatePassword(userDto.getPassword()));
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(UUID userId) {
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