//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.UserService;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//public class JCFUserService implements UserService {
//    private final Map<UUID, User> data;
//
//    public JCFUserService() {
//        this.data = new HashMap<>();
//    }
//
//    @Override
//    public User create(String username, String email, String password,String phoneNumber,String address) {
//        User user = new User(username, email, password, phoneNumber, address);
//        this.data.put(user.getId(), user);
//
//        return user;
//    }
//
//    @Override
//    public User findById(UUID userId) {
//        User userNullable = this.data.get(userId);
//
//        return Optional.ofNullable(userNullable)
//                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
//    }
//
//    @Override
//    public List<User> findAll() {
//        return this.data.values().stream().toList();
//    }
//
//    @Override
//    public User update(UUID userId, String field,Object value) {
//        User userNullable = this.data.get(userId);
//        User user = Optional.ofNullable(userNullable)
//                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
//        switch (field) {
//            case "username" -> user.update((String) value, null, null, null, null);
//            case "email" -> user.update(null, (String) value, null, null, null);
//            case "password" -> user.update(null, null, (String) value, null, null);
//            case "phoneNumber" -> user.update(null, null, null, (String) value, null);
//            case "address" -> user.update(null, null, null, null, (String) value);
//            default -> throw new IllegalArgumentException("Invalid field: " + field);
//        }
//        return user;
//    }
//
//    @Override
//    public void delete(UUID userId) {
//        if (!this.data.containsKey(userId)) {
//            throw new NoSuchElementException("User with id " + userId + " not found");
//        }
//        this.data.remove(userId);
//    }
//}
