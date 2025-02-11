package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

public class JCFUserService implements UserService {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final JCFUserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new JCFUserRepository();
    }

    @Override
    public void registerUser(User user) {
        try {
            if (!isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다. 바꿔주세요");
            } else if (userRepository.findById(user.getId()).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 사용자입니다.");
            } else {
                userRepository.save(user);
                System.out.println("사용자가 등록되었습니다.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateUserName(User user, String newName) {
        try {
            User existingUser = getUserOrThrow(user);
            existingUser.updateUserName(newName);
            System.out.println("사용자 이름이 수정되었습니다.");
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateUserEmail(User user, String newEmail) {
        try {
            if (!isValidEmail(newEmail)) {
                System.out.println("올바르지 않은 이메일 형식입니다. 바꿔주세요");
            } else {
                User existingUser = getUserOrThrow(user);
                existingUser.updateUserEmail(newEmail);
                System.out.println("사용자 이메일이 수정되었습니다.");
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    @Override
    public void deleteUser(User user, String password) {
        try {
            User existingUser = getUserOrThrow(user);
            if (existingUser.getPassword().equals(password)) {
                userRepository.delete(existingUser);
                System.out.println("사용자가 삭제되었습니다.");
            } else {
                System.out.println("비밀번호가 일치하지 않습니다.");
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getUser(User user) {
        try {
            User existingUser = getUserOrThrow(user);
            existingUser.display();
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getAllUser() {
        List<User> users = userRepository.load();
        if (users.isEmpty()) {
            System.out.println("등록된 사용자가 없습니다.");
        } else {
            users.forEach(User::display);
        }
    }

    private User getUserOrThrow(User user) {
            return userRepository.findById(user.getId())
                    .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."));
    }
}
