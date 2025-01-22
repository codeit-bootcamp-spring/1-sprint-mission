package com.srint.mission.discodeit.repository.file;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "userData.ser";
    private Map<UUID, User> data;

    public FileUserRepository() {
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
        if(!data.containsKey(user.getId())){
            data.put(user.getId(), user);
        }
        saveDataToFile();
        return user.getId();
    }

    public User findOne(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("조회할 User를 찾지 못했습니다.");
        }
        return data.get(id);
    }

    public List<User> findAll() {
        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return new ArrayList<>(data.values());
    }

    public UUID delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 User를 찾지 못했습니다.");
        }
        data.remove(id);
        saveDataToFile();
        return id;
    }
}
