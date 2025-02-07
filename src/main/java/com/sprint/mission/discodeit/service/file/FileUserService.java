package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.DateUtils;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {


    private static final String fileName = "savedata/user.ser";
    private final Map<UUID, User> userList;

    public FileUserService() {
        this.userList = loadFile();
    }

    @Override
    public User createUser(String userName) { // 유저 추가
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 null 또는 빈 문자열일 수 없습니다.");
        }
        User user = new User(userName);
        userList.put(user.getId(), user);
        System.out.println(DateUtils.transTime(user.getCreatedAt()) + " " + user.getUserName() + " 유저가 생성되었습니다.");
        saveFile();
        return user;
    }

    @Override
    public User readUser(UUID id) { // 유저 읽기
        return Optional.ofNullable(userList.get(id))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public List<User> readAllUser() { // 모든 유저 읽기
        return new ArrayList<>(userList.values());
    }

    @Override
    public User modifyUser(UUID userID, String newName) { // 유저 수정
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 null 또는 빈 문자열일 수 없습니다.");
        }
        User user = readUser(userID);
        String oriName = user.getUserName();
        user.updateUsername(newName);
        user.updateUpdatedAt();
        System.out.println("유저 이름 변경: \"" + oriName + "\" -> \"" + newName + "\"");
        saveFile();
        return user;
    }

    @Override
    public void deleteUser(UUID id) { // 유저 삭제
        User user = readUser(id);
        if (userList.remove(id) != null) {
            System.out.println(user.getUserName() + " 유저가 삭제되었습니다.");
            saveFile();
        } else {
            System.out.println(user.getUserName() + " 유저 삭제 실패하였습니다.");
        }
    }

    //저장로직

    private void saveFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(userList);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }

    }

    private Map<UUID, User> loadFile(){

        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }

}
