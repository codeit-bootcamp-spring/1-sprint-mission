package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.util.*;

public class UserStatusFileRepositoryImpl implements UserStatusRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\UserStatus.ser";

    @Override
    public UserStatus save(UserStatusDto userStatusDto) {
        Map<UUID, UserStatus> temp = loadFromSer(FILE_NAME);
        UserStatus userStatus = new UserStatus(userStatusDto.userId());
        temp.put(userStatus.getId(), userStatus);
        saveToSer(FILE_NAME, temp);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        Map<UUID, UserStatus> temp = loadFromSer(FILE_NAME);
        UserStatus userStatus = temp.get(id);
        userStatus = userStatus.checkStatus(); //Status : OFFLINE, ONLINE 다시 체크
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        Map<UUID, UserStatus> temp = loadFromSer(FILE_NAME);
        UserStatus findUserStatus = temp.values().stream().filter(userStatus -> userStatus.getUserId().equals(userId)).findFirst().orElse(null);
        if( findUserStatus != null ) findUserStatus = findUserStatus.checkStatus(); //Status : OFFLINE, ONLINE 다시 체크
        return findUserStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, UserStatus> temp = loadFromSer(FILE_NAME);
        temp.remove(id);
        saveToSer(FILE_NAME, temp);
    }

    @Override
    public void update(UserStatusDto before, UserStatusDto after) {
        Map<UUID, UserStatus> temp = loadFromSer(FILE_NAME);
        UserStatus updated = findById(before.id()).updateFields(after);
        temp.replace(updated.getId(), updated);
        saveToSer(FILE_NAME, temp);

    }
    private void saveToSer(String fileName, Map<UUID, UserStatus> userStatusMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(userStatusMap); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, UserStatus> loadFromSer(String fileName) {
        Map<UUID, UserStatus> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, UserStatus>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }



}
