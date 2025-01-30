package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class FileUserService implements UserService {
    private final FileUserRepository userRepository;
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public FileUserService(){
        this.userRepository = new FileUserRepository();
        userRepository.init();
    }

    @Override
    public void registerUser(User user){
        try {
            List<User> users = userRepository.load();
            if (!isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
            } else if (users.stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
                throw new IllegalArgumentException("이미 등록된 사용자입니다.");
            } else {
                users.add(user);
                userRepository.save(users);
                System.out.println("사용자가 등록되었습니다.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateUserName(User user, String newName){
        try {
            List<User> users = userRepository.load();
            User existingUser = getUserOrThrow(user, users);
            existingUser.setUserName(newName);
            userRepository.save(users);
            System.out.println("사용자 이름이 수정되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateUserEmail(User user, String newEmail){
        try {
            List<User> users = userRepository.load();
            if (!isValidEmail(newEmail)) {
                throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
            } else {
                User existingUser = getUserOrThrow(user, users);
                existingUser.setEmail(newEmail);
                userRepository.save(users);
                System.out.println("사용자 이메일이 수정되었습니다.");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    @Override
    public void deleteUser(User user, String password){
        try {
            List<User> users = userRepository.load();
            User existingUser = getUserOrThrow(user, users);
            if (existingUser.getPassword().equals(password)) {
                users.remove(existingUser);
                userRepository.save(users);
                System.out.println("사용자가 삭제되었습니다.");
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getUserInfo(User user){
        try {
            List<User> users = userRepository.load();
            User existingUser = getUserOrThrow(user, users);
            existingUser.display();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getAllUser(){
        try {
            List<User> users = userRepository.load();
            if (users.isEmpty()) {
                throw new NoSuchElementException("등록된 사용자가 없습니다.");
            } else {
                users.forEach(User::display);
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    private User getUserOrThrow(User user, List<User> users) {
        return users.stream()
                .filter(existingUser -> existingUser.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."));
    }
}
