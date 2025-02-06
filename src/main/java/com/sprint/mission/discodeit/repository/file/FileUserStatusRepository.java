package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@Primary
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String USER_STATUS_JSON_FILE = "tmp/userStatuses.json";
    private final ObjectMapper mapper;
    private Map<UUID, UserStatus> userStatusMap;
    
    public FileUserStatusRepository() {
        //Json 파싱을 위해 ObjectMapper 생성
        mapper = new ObjectMapper();
        userStatusMap = new HashMap<>();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusMap.put(userStatus.getUserId(), userStatus);
        saveUserStatusesToJson(userStatusMap);
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusMap.get(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusMap.remove(userId);
        saveUserStatusesToJson(userStatusMap);
    }

    private Map<UUID, UserStatus> loadUserStatusesFromJson() {
        Map<UUID, UserStatus> map = new HashMap<>();
        File file = new File(USER_STATUS_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, UserStatus>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveUserStatusesToJson(Map<UUID, UserStatus> userStatusMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_STATUS_JSON_FILE), userStatusMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
