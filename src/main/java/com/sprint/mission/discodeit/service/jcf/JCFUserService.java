//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.view.ConsoleView;
//
//import java.util.*;
//
//public class JCFUserService implements UserService {
//
//    private final List<User> users = new ArrayList<>();
//    private final ConsoleView view;
//
//    public JCFUserService(ConsoleView view) {
//        this.view = view;
//    }
//
//    @Override
//    public User createUser(String username, String email, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username)) {
//                view.displayError("이미 존재하는 유저입니다: " + username);
//                return null;
//            }
//        }
//        User user = new User(username, email, password);
//        users.add(user);
//        view.displaySuccess("유저가 생성되었습니다: " + username);
//        return user;
//    }
//
//
//    @Override
//    public User getUserById(UUID userId) {
//        for (User user : users) {
//            if (user.getId().equals(userId)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<User> getAllUsers(){
//        return new ArrayList<>(users);
//    }
//
//
//    @Override
//    public User updateUsername(UUID userId, String newUsername) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updateUsername(newUsername);
//            view.displaySuccess("유저 이름이 업데이트되었습니다: " + newUsername);
//        }
//        return user;
//    }
//
//    @Override
//    public User updateEmail(UUID userId, String newEmail) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updateEmail(newEmail);
//            view.displaySuccess("이메일이 업데이트되었습니다: " + newEmail);
//        }
//        return user;
//    }
//
//    @Override
//    public User updatePassword(UUID userId, String newPassword) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updatePassword(newPassword);
//            view.displaySuccess("비밀번호가 업데이트되었습니다");
//        }
//        return user;
//    }
//
//    @Override
//    public boolean deleteUser(UUID userId) {
//        User user = getUserById(userId);
//        if (user != null) {
//            users.remove(user);
//            view.displaySuccess("유저가 삭제되었습니다");
//            return true;
//        }
//        return false;
//    }
//}
