package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private final String FILE_PATH = "tmp/user.ser";
    private Map<UUID, User> data;
    private static volatile FileUserService userService;

    public FileUserService() {
       this.data = loadDataFromFile();
    }

    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드에 실패했습니다 : " + e.getMessage());
        }
    }

    //싱글톤 패턴 사용
    public static FileUserService getInstance() {
        if (userService == null) {
            synchronized (FileUserService.class) {
                if (userService == null) {
                    userService = new FileUserService();
                }
            }
        }
        return userService;
    }


    private void saveDataToFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일에 데이터 저장 작업을 실패했습니다." + e.getMessage());
        }
    }
    @Override
    public User createUser(String name, String email) {
        if (correctName(name) && correctEmail(email)) {
            User newUser = new User(name, email);
            data.put(newUser.getId(), newUser);
            saveDataToFile();
            System.out.println("환영합니다!" + newUser.getUserName() + "님 반갑습니다~");
            return newUser;
        }
        System.out.println("유효하지 않은 이름 또는 이메일입니다.");
        return null;
    }

    @Override
    public void updateUserName(UUID userId, String newName) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
            return;
        }
        if (correctName(newName)) {
            user.setUserName(newName);
            saveDataToFile();
            System.out.println("사용자의 이름이 수정되었습니다.");
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String newEmail) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
            return;
        }
        if(correctEmail(newEmail)) {
            user.setUserEmail(newEmail);
            saveDataToFile();
            System.out.println("사용자의 이메일이 성공적으로 변경되었습니다.");
        }

    }

    private boolean correctName(String name){
        if(name.isBlank()) {
            System.out.println("이름을 입력해주세요");
        } else if(name.length() < 2) {
            System.out.println("이름은 두글자 이상 입력해주세요!");
        }else  {
            return  true;
        }
        return false;
    }
    private boolean correctEmail(String email){
        String emailFormat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(!email.matches(emailFormat)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
        }
        return email.matches(emailFormat);
    }

    @Override
    public List<User> getAllUserList() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public User searchById(UUID userId) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
        }
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = data.remove(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
        } else  {
            saveDataToFile();
            System.out.println("사용자가 정상적으로 삭제되었습니다.");
        }
    }

}
