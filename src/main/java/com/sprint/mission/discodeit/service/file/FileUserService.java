//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.UUID;
//import java.io.ObjectInputStream;
//import java.io.FileInputStream;
//
////import java.util.*;
////import java.io.*;
///*
//todo
//    1. 예외, 오류 처리 할 것
//    2. 마스터 임포트 해결
//    3. equals 재정의 필요?
//
// */
//
//
//public class FileUserService implements UserService {
//    private static final String FILE_PATH = "files/users.ser";
//    private List<User> users;
//
//    public FileUserService() {
//        File directory = new File("files");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        this.users = loadFromFile();
//    }
//
//    @Override
//    public User createUser(String username, String email, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username)) {
//                System.out.println("이미 존재하는 유저입니다: " + username);
//                return null;
//            }
//        }
//        User user = new User(username, email, password);
//        users.add(user);
//        saveToFile();
//        return user;
//    }
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
//    public List<User> getAllUsers() {
//        return new ArrayList<>(users);
//    }
//
//    @Override
//    public User updateUsername(UUID userId, String newUsername) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updateUsername(newUsername);
//            saveToFile();
//        }
//        return user;
//    }
//
//    @Override
//    public User updateEmail(UUID userId, String newEmail) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updateEmail(newEmail);
//            saveToFile();
//        }
//        return user;
//    }
//
//    @Override
//    public User updatePassword(UUID userId, String newPassword) {
//        User user = getUserById(userId);
//        if (user != null) {
//            user.updatePassword(newPassword);
//            saveToFile();
//        }
//        return user;
//    }
//
//    @Override
//    public boolean deleteUser(UUID userId) {
//        User user = getUserById(userId);
//        if (user != null) {
//            users.remove(user);
//            saveToFile();
//            return true;
//        }
//        return false;
//    }
//
//    private void saveToFile() {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
//            oos.writeObject(users);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<User> loadFromFile() {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
//            return (List<User>) ois.readObject();
//        } catch (Exception e) {
//            return new ArrayList<>();
//        }
//    }
//
//}
