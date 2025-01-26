package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "tmp/user.ser";
    private Map<UUID, User> userMap;
    public FileUserRepository(){
        this.userMap = loadFromFile();
    }
    private Map<UUID, User> loadFromFile() {
        userMap = new HashMap<>();
        File chatFile = new File(FILE_PATH);

        // 파일이 없을 때 파일을 생성
        if (!chatFile.exists()) {
            System.out.println("파일이 존재하지 않아 새로 생성합니다.");
            return new HashMap<>(); //빈 map
        }

        // 파일이 비어 있는 경우
        if (chatFile.length() == 0) {
            System.out.println("파일이 비어 있으므로 빈 맵을 반환합니다.");
            return new HashMap<>();
        }

        // 직렬화된 객체 읽기
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chatFile))) {
            return (Map<UUID, User>) ois.readObject();
        }catch (IOException | ClassNotFoundException e) {
            System.out.println("데이터 로드 중 오류 발생 : " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userMap);
            System.out.println("데이터가 잘 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    @Override
    public User createUser(String name, String email) {
        boolean userExists = userMap.values().stream().anyMatch(existingUser -> existingUser.getUserName()
                .equals(name) && existingUser.getUserEmail().equals(email));

        if(userExists) {
            System.out.println("이미 존재하는 유저입니다.");
            return null;
        }
        User user = new User(name, email);
        userMap.put(user.getId(), user);
        saveToFile();
        System.out.println("환영합니다. 회원가입 완료되었습니다 : " + user.getUserName());
        return user;
    }


    @Override
    public List<User> getAllUserList() {
        userMap = loadFromFile();
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User searchById(UUID userId) {
        User user = userMap.get(userId);
        if(user == null) {
            System.out.println("해당 사용자를 검색할 수 없습니다 : " + userMap);
        }
        return user;
    }

    @Override
    public void updateUserName(UUID userId, String name) {
        User user = userMap.get(userId);
        if(user == null) {
            System.out.println("사용자를 찾을 수 없습니다 : " + userMap);
            return;
        }
        user.setUserName(name);
        saveToFile();
        System.out.println("유저의 이름이 성공적으로 수정되었습니다. : " + user.getUserName());

    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        User user = userMap.get(userId);
        if(user == null) {
            System.out.println("사용자를 찾을 수 없습니다 : " + userMap);
            return;
        }
        user.setUserEmail(email);
        saveToFile();
        System.out.println("유저의 이메일이 성공적으로 수정되었습니다. : " + user.getUserEmail());

    }

    @Override
    public void deleteUser(UUID userId) {
        User removedUser = userMap.remove(userId);
        if(removedUser == null) {
            System.out.println("사용자를 찾을 수 없습니다 : " + userMap);
        } else {
            saveToFile();
            System.out.println("유저가 성공적으로 삭제되었습니다 : " + userMap);
        }

    }
}
