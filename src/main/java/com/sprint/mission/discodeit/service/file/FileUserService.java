package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "users.ser";

    public FileUserService() {
        // 초기화 로직을 여기서 수행
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            resetFile(); // 파일이 없을 때만 초기화
        }
    }

    public void resetFile() {
        File file = new File(FILE_PATH);
        if (file.exists() && file.delete()) {
            System.out.println("파일이 초기화되었습니다."); // 초기화 메시지
        }
        saveAllUsers(new ArrayList<>()); // 초기화 후 빈 사용자 목록 저장
    }



    private void saveAllUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("사용자 목록 저장 중 오류 발생", e);
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public List<User> readAllUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>(); // 파일이 비어있을 경우 빈 리스트 반환
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 목록 읽기 중 오류 발생", e);
        }
    }


    @Override
    public void createUser(User user) {
        List<User> users = readAllUsers();
        if (users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + user.getId());
        }
        users.add(user);
        saveAllUsers(users);
        System.out.println("사용자가 생성되었습니다: " + user);
    }

    @Override
    public User readUser(String id) {
        List<User> users = readAllUsers();

        System.out.println("파일에서 읽은 사용자 목록:");
        users.forEach(user -> System.out.println("User ID: " + user.getId()));

        UUID uuid = UUID.fromString(id);
        System.out.println("검색하려는 UUID: " + uuid);

        return users.stream()
                .filter(user -> user.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
    }

    @Override
    public void updateUser(User user) {
        List<User> users = readAllUsers();
        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + user.getId());
        }

        saveAllUsers(users);
        System.out.println("사용자가 업데이트되었습니다: " + user);
    }

    @Override
    public void deleteUser(String id) {
        List<User> users = readAllUsers();

        if (!users.removeIf(user -> user.getId().equals(UUID.fromString(id)))) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + id);
        }

        saveAllUsers(users);
        System.out.println("사용자가 삭제되었습니다: " + id);
    }
}
