package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;


    //모든 유저 관리 맵 'UsersMap' 반환
    @Override
    public HashMap<UUID, User> getUsersMap() {
        return userRepository.getUsersMap();
    }

    //해당 채널 리턴
    @Override
    public User getUserById(UUID userId) {
        if (userId == null || userRepository.isUserExist(userId)==false){
            System.out.println("유저 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return userRepository.getUser(userId);
    }

    @Override
    public String getUserNameById(UUID userId) {
        if (userId == null || userRepository.isUserExist(userId)==false){
            System.out.println("유저이름 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return userRepository.getUser(userId).getUserName();
    }

    //유저 생성.
    @Override
    public UUID createUser(String userName, String email,String password) {
        if (userName == null || email == null || password == null) {
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        User newUser = new User(userName, email, password);
        userRepository.addUser(newUser);
        System.out.println(userName + " 유저 생성 성공!");
        return newUser.getId();
    }

    //유저 생성.
    /*@Override
    public UUID createUser(String userName, String email,String password, String profilePicturePath) {
        if (userName == null || email == null || password == null || profilePicturePath == null) {
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        File profilePicture =
        User newUser = new User(userName, email, password, profilePicture);
        userRepository.addUser(newUser);
        System.out.println(userName + " 유저 생성 성공!");
        return newUser.getId();
    }*/

    //유저 존재여부 확인.
    @Override
    public boolean isUserExist(UUID userId) {
        if (userId == null){
            return false;
        }
        return userRepository.isUserExist(userId);
    }

    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId) {
        if (userId == null|| userRepository.isUserExist(userId)==false){
            System.out.println("유저 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        userRepository.deleteUser(userId);
        System.out.println("유저 삭제 성공!");
        return true;
    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null || userRepository.isUserExist(userId)==false){
            System.out.println("유저 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        userRepository.getUser(userId).setUserName(newName);
        System.out.println("유저 이름 변경 성공!");
        return true;

    }


}
