package com.sprint.mission.service.file;

import com.sprint.mission.entity.User;
import com.sprint.mission.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "src/main/resources/users.ser";
    private Map<String, User> userData;

    public FileUserService() {
        this.userData = loadData();
    }

    // 계정 생성
    @Override
    public User createUser(String email, String name) {
        if(userData.containsKey(email)){
            System.out.println("\n**이미 존재하는 이메일입니다.**");
            return null;
        }
        User newUser = new User(UUID.randomUUID(), email, name);
        userData.put(email, newUser);
        saveData();
        System.out.println("\n***계정 생성 완료***");
        return newUser;
    }

    // 이메일 변경
    @Override
    public void updateMail(User user, String mail) {
        if (userData.containsKey(mail)) {
            System.out.println("\n**이미 존재하는 이메일입니다.**");
            return;
        }
        if(userData.containsValue(user)) {
            userData.remove(user.getEmail());
            user.setEmail(mail);
            userData.put(mail, user);
            saveData();
            System.out.println("\n***이메일 변경 완료***");
        }else{
            System.out.println("\n**유저를 찾을 수 없습니다.**");
        }
    }

    // 모든 유저 조회
    @Override
    public List<User> findAllUserList() {
        System.out.println("\n***사용자 정보 전부 조회***");
        for (User user : userData.values()) {
            System.out.println(user);
        }
        return new ArrayList<>(userData.values());
    }

    // 특정 유저 조회
    @Override
    public void searchUser(User user) {
        if (userData.containsValue(user)) {
            System.out.println("\n***[사용자 정보]***");
            System.out.println(user);
        } else {
            System.out.println("\n**유저를 찾을 수 없습니다.**");
        }
    }

    // 유저 삭제
    @Override
    public void deleteUser(User user) {
        if (userData.containsValue(user)) {
            userData.remove(user.getEmail());
            saveData();
            System.out.println("\n***계정 삭제 완료***");
        } else {
            System.out.println("\n**유저를 찾을 수 없습니다.**");
        }
    }

    // 파일에 데이터 저장
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userData);
            System.out.println("===데이터가 저장되었습니다: " + FILE_PATH + "===");
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage() + "===");
        }
    }

    private Map<String, User> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("===파일이 존재하지 않음. 새로 생성됩니다.===");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===파일 로드 중 오류 발생: " + e.getMessage() + "===");
            return new HashMap<>();
        }
    }
    public void resetData() {
        this.userData = new HashMap<>();
        saveData();
        System.out.println("===데이터가 초기화되어 빈 상태로 저장되었습니다.===");
    }

}