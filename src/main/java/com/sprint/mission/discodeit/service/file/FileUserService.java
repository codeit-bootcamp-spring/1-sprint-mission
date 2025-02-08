package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    private static final String FILE_PATH = "userData.ser";
    private Map<UUID, User> data;

    public FileUserService() {
        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 데이터 파일에 쓰기
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = new File(FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        }
    }

    public UUID save(User user) {
        data.put(user.getId(), user);
        saveDataToFile();
        return user.getId();
    }

    public User findOne(UUID id) {
        return data.get(id);
    }

    public List<User> findAll() {
/*        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(User user){
        data.put(user.getId(), user);
        saveDataToFile();
        return user.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }

    //서비스 로직
    @Override
    public UUID create(String username, String email, String password) {
        if(!User.validation(username, email, password)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        User user = new User(username, email, password);
        return save(user);
    }

    @Override
    public User read(UUID id) {
        User findUser = findOne(id);
        return Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    @Override
    public List<User> readAll() {
        return findAll();
    }

    @Override
    public User updateUser(UUID id, String name, String email){
        User findUser = findOne(id);
        findUser.setUser(name, email);
        update(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = findOne(id);
        return delete(findUser.getId());
    }

}
