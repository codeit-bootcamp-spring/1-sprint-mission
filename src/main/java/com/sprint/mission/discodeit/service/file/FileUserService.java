package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.lang.reflect.Member;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "temp/users-obj.ser";
    private final Map<UUID,User> data=new HashMap<>();

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
        saveData();
    }

    @Override
    public Optional<User> findUser(UUID id) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object findObject = ois.readObject();
            Map<UUID, User> readData = (Map<UUID, User>) findObject;
            return Optional.ofNullable(readData.get(id));
        } catch (FileNotFoundException e) {
            System.out.println("지정된 파일 없음" + e.getMessage());
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> lists=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            lists.add(data.get(uuid));
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("temp/all-users-obj.ser"))) {
            oos.writeObject(lists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("temp/all-users-obj.ser"))) {
            Object findObject = ois.readObject();
            return (List<User>) findObject;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUserName(UUID id, String userName) {
        if(data.containsKey(id)) {
            data.get(id).updateUserName(userName);
            saveData();
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void updateUserEmail(UUID id, String userEmail) {
        if(data.containsKey(id)) {
            data.get(id).updateUserEmail(userEmail);
            saveData();
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
            saveData();
        }else{
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
