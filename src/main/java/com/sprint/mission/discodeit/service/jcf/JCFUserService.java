package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class JCFUserService implements UserService {

    private static final JCFUserService jcfUserService = new JCFUserService();
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>(1000);
    }

    public static JCFUserService getInstance() {
        return jcfUserService;
    }

    @Override
    public User createUser(UserDto userDto) {
        String hashedPassword = generatePassword(userDto.getPassword());

        User user = User.of(userDto.getName(), userDto.getLoginId(), hashedPassword);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User readUser(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user입니다."));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID userId, UserDto userDto) {
        User user = readUser(userId);

        user.updateLoginId(userDto.getLoginId());
        user.updatePassword(generatePassword(userDto.getPassword()));
        user.updateName(userDto.getName());
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
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
