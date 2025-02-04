package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private static final String FILE_EXTENSION = ".ser";

    public Path directory = Paths.get(System.getProperty("user.dir"), "data/user");
    private final FileManager fileManager;

    public FileUserService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // 생성
    public User createUser(String username, String password, String email) {
        fileManager.init(directory);
        User user = new User(username, password, email);
        Path filePath = directory.resolve(user.getId().toString().concat(FILE_EXTENSION));
        fileManager.save(filePath, user);
        System.out.println("Created user " + username);
        return user;
    }

    // 정보 수정
    public User updateUserName(User user, String username) {
        user.updateName(username);
        fileManager.save(directory.resolve(user.getId().toString().concat(FILE_EXTENSION)), user);
        System.out.println("User name updated");
        return user;
    }

    public User updatePassword(User user, String password) {
        user.updatePassword(password);
        fileManager.save(directory.resolve(user.getId().toString().concat(FILE_EXTENSION)), user);
        System.out.println("User password updated");
        return user;
    }

    public User updateEmail(User user, String email) {
        user.updateEmail(email);
        fileManager.save(directory.resolve(user.getId().toString().concat(FILE_EXTENSION)), user);
        System.out.println("User email updated");
        return user;
    }

    // 조회
    public User findUserById(User u) {
        return fileManager.load(directory.resolve(u.getId().toString().concat(FILE_EXTENSION)), User.class);
    }

    public List<User> findAllUsers() {
        List<User> userList = fileManager.allLoad(directory);
        return new ArrayList<>(userList);
    }

    // 유저 프린트
    public void printUser(User user) {
        System.out.println(user);
    }

    public void printListUsers(List<User> users) {
        users.forEach(System.out::println);
    }

    // 삭제
    public void deleteUserById(User user) {
        List<User> userList = fileManager.allLoad(directory);

        for (User targetUser : userList) {
            if (targetUser.getId().equals(user.getId())) {
                System.out.println(targetUser.getUsername() + " User deleted");
                fileManager.delete(directory.resolve(targetUser.getId().toString().concat(FILE_EXTENSION)));
            }
        }
    }

}
