package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\users.ser";

    @Override
    public UUID save(String userName) {
        Map<UUID, User> userMap = loadFromSer(FILE_NAME);
        if (userMap.values().stream().anyMatch(user -> user.getUserName().equals(userName))) {
            User user = userMap.get(userMap.keySet().stream().findFirst().get());
            System.out.println("이미 존재하는 사용자입니다.");
            return user.getUserId(); // 중복된 사용자 이름이 있으면 처리 중단
        }
        User user = new User(userName);
        userMap.put(user.getUserId(), user);
        saveToSer(FILE_NAME, userMap);

        return user.getUserId();
    }

    @Override
    public User findUserById(UUID id) {
        return loadFromSer(FILE_NAME).get(id);
    }

    @Override
    public List<User> findAll() {
        List<User> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID id) {
        Map<UUID, User> userMap = loadFromSer(FILE_NAME);
        if(userMap.containsKey(id)){
            /*List<Message> messagesByUserId = fileMessageService.getMessagesByUserId(id);
            for (Message message : messagesByUserId) {
                fileMessageService.deleteMessage(message.getMessageId());
            }*/
            userMap.remove(id);
            saveToSer(FILE_NAME, userMap);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void update(UUID id, String username) {
        Map<UUID, User> userMap = loadFromSer(FILE_NAME);
        if(userMap.containsKey(id)){
            System.out.println("유저 수정 중");
            User user = findUserById(id);
            userMap.remove(user.getUserId());
            user.update(username);
            userMap.put(user.getUserId(), user);
            saveToSer(FILE_NAME, userMap);

        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

    private static void saveToSer(String fileName, Map<UUID, User> userData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(userData); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<UUID, User> loadFromSer(String fileName) {
        Map<UUID, User> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, User>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }
}
