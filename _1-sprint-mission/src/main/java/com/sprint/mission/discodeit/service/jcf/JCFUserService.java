package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.*;

public class JCFUserService implements UserService {
    //Repository 의존성 주입
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 생성
    @Override
    public User create(String username,String email,String password,String phoneNumber,String address){ //사용자 생성{
        User user = new User(username,email,password,phoneNumber,address); //사용자 생성
        return userRepository.create(user);
    }
    // 단일조회
    @Override
    public User find(UUID userId) {
        try{
            User userNullable = userRepository.findById(userId);
            if(userId == null){
                throw new NoSuchElementException("UserId not found");
            }
            return userNullable;
        }catch (NoSuchElementException e){
            System.out.println("Error: "+e.getMessage());
            throw e;
        }
    }
    // 다중조회
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }
    // 업데이트
    @Override
    public User update(UUID userId,String field,Object value) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }

        switch (field.toLowerCase()) {
            case "username":
                user.update((String) value, null, null, null, null);
                break;
            case "email":
                user.update(null, (String) value, null, null, null);
                break;
            case "password":
                user.update(null, null, (String) value, null, null);
                break;
            case "phonenumber":
                user.update(null, null, null, (String) value, null);
                break;
            case "address":
                user.update(null, null, null, null, (String) value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return userRepository.create(user); // 업데이트된 유저 반환
    }
    // 삭제
    @Override
    public void delete(UUID userId) {
        if(userRepository.findById(userId)==null){
            throw new NoSuchElementException("UserId not found");
        }
        userRepository.delete(userId);
    }
}
