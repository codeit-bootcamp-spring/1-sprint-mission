package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "file", matchIfMissing = true)
public class FileUserRepository implements UserRepository, FileService<User> {
    private static final String USER_SAVE_FILE = "config/user.ser";

    @Override
    public Map<UUID, User> loadFromFile() {
        File file = new File(USER_SAVE_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();

            if (data instanceof Map) {
                return (Map<UUID, User>) data;
            } else if (data instanceof List) {
                List<User> users = (List<User>) data;

                Map<UUID, User> userMap = new HashMap<>();
                for (User user : users) {
                    userMap.put(user.getId(), user);
                }
                return userMap;
            } else {
                throw new IllegalStateException("Unknown data format in file");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
//    public Map<UUID, User> loadFromFile() {
//        File file = new File(USER_SAVE_FILE);
//        if(!file.exists()){
//            return new HashMap<>();
//        }
//
//        try (FileInputStream fis = new FileInputStream(file);
//             ObjectInputStream ois = new ObjectInputStream(fis)
//        ){
//            return (Map<UUID, User>) ois.readObject();
//        } catch (IOException | ClassNotFoundException e){
//            e.printStackTrace();
//            return new HashMap<>();
//        }
//    }

    @Override
    public boolean saveToFile(Map<UUID, User> data) {
        try (FileOutputStream fos = new FileOutputStream(USER_SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(data);
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(User user){
        Map<UUID, User> users = loadFromFile();
        users.put(user.getId(), user);
        saveToFile(users);
        return true;
    }

    @Override
    public User findById(UUID id) {
        return loadFromFile().get(id);
//        return loadFromFile().stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst()
//                .orElse(null);
    }

    @Override
    public List<User> readAll(){
        return new ArrayList<>(loadFromFile().values());
    }

    @Override
    public User modify(UUID id, User modifiedUser){
        Map<UUID, User> users = loadFromFile();

        if (users.containsKey(id)) {
            User existingUser = users.get(id);
            // 기존 사용자 정보를 수정
            existingUser.setUsername(modifiedUser.getUsername());
            existingUser.setPassword(modifiedUser.getPassword());
            existingUser.setEmail(modifiedUser.getEmail());
            existingUser.setPhoneNumber(modifiedUser.getPhoneNumber());
            existingUser.update();
            saveToFile(users);
            return modifiedUser;
        } else {
            System.out.println("아이디가 존재하지 않습니다.");
            return null;
        }
    }

    @Override
    public boolean deleteById(UUID id){
            Map<UUID, User> users = loadFromFile();
            if(users.remove(id) != null){
                saveToFile(users);
                return true;
            }else{
                System.out.println("유효하지 않은 ID 입니다..\n");
                return false; // ID를 찾지 못했을 때 false 반환
            }
    }
}
