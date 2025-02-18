package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
@Repository
@Profile("File")

public class FileUserRepository implements UserRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\users.ser";

    @Override
    public User save(String userName, String password,  String email) { //저장로직만 있어야하는데? 의문
        Map<UUID, User> userMap = loadFromSer(FILE_NAME);
        User user = new User(userName,password, email);
        userMap.put(user.getId(), user);
        saveToSer(FILE_NAME, userMap);

        return user;
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
            userMap.remove(id);
            saveToSer(FILE_NAME, userMap);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void update(UUID id, String username, String email) {
        Map<UUID, User> userMap = loadFromSer(FILE_NAME);
        if(userMap.containsKey(id)){
            System.out.println("유저 수정 중");
            User user = findUserById(id);
            userMap.remove(user.getId());
            user.update(username, email);
            userMap.put(user.getId(), user);
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
