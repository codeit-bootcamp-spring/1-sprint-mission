package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String USER_JSON_FILE = "tmp/users.json";
    private final ObjectMapper mapper;
    private Map<UUID, User> userMap;


    public FileUserRepository() {
        //Json 파싱을 위해 ObjectMapper 생성
        mapper = new ObjectMapper();
        userMap = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userMap = loadUserFromJson();
        userMap.put(user.getId(), user);
        saveUsersToJson(userMap);
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
    public void delete(UUID userId) {
        userMap = loadUserFromJson();
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
            saveUsersToJson(userMap);
        }
    }

    private Map<UUID, User> loadUserFromJson() {
        Map<UUID, User> map = new HashMap<>();
        File file = new File(USER_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            // JSON 파일 읽어 Map 형태로 변환
            map = mapper.readValue(file, new TypeReference<Map<UUID, User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveUsersToJson(Map<UUID, User> userMap) {
        try {
            // json 데이터를 보기좋게(pretty print) 정렬하여 저장
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_JSON_FILE), userMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}