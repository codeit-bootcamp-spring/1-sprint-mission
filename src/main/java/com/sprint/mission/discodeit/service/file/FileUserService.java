package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.UserUtil.validEmail;
import static com.sprint.mission.discodeit.util.UserUtil.validUsername;

public class FileUserService implements UserService {
    private static final String USER_JSON_FILE = "tmp/users.json";
    private final ObjectMapper mapper;
    private Map<UUID, User> userMap;

    public FileUserService() {
        userMap = new HashMap<>();
        mapper = new ObjectMapper();
    }

    @Override
    public User create(String username, String email, String phoneNumber, String password) {
        validUsername(username);
        validEmail(email);

        User user = new User(username, email, phoneNumber, password);
        saveUserToJson(user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return loadUserFromJson().get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadUserFromJson().values());
    }

    @Override
    public User update(UUID userId, String username, String email, String phoneNumber, String password) {
        if (!loadUserFromJson().containsKey(userId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        validUsername(username);
        validEmail(email);

        User checkUser = loadUserFromJson().get(userId);
        checkUser.update(username, email, phoneNumber, password);
        saveUserToJson(checkUser);
        return checkUser;
    }

    @Override
    public void delete(UUID userId) {
        userMap = loadUserFromJson();
        if (!userMap.containsKey(userId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        userMap.remove(userId);
    }

    private Map<UUID, User> loadUserFromJson() {
        File file = new File(USER_JSON_FILE);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        try {
            return Arrays.asList(mapper.readValue(file, User[].class))
                    .stream()
                    .collect(Collectors.toMap(User::getId, user -> user));
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveUserToJson(User user) {
        userMap = loadUserFromJson();
        userMap.put(user.getId(), user);
        saveAllUsersToJson(userMap);
    }

    private void saveAllUsersToJson(Map<UUID, User> users) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_JSON_FILE), users.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
