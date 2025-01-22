package com.sprint.mission.discodeit.service.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService{

    private final UserRepository fileUserRepository;
    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public FileUserService(FileUserRepository fileUserRepository, InputHandler inputHandler){
        this.fileUserRepository = fileUserRepository;
        this.inputHandler = inputHandler;
    }

    public UUID createUser(String nickname){
        User user = new User(nickname);
        fileUserRepository.saveUser(user);
        return user.getId();
    }

    // Read : 전체 유저 조회, 특정 유저 조회
    public int showAllUsers(){
        // users/ 아래 저장된 모든 파일을 불러온다.
        // fileUserRepository.getAllUsers();
        return fileUserRepository.getAllUsers().size();
    }
    public User getUserById(UUID id){
        return fileUserRepository.findUserById(id).orElse(null);
    }

    // Update : 특정 유저 닉네임 변경
    public void updateUserNickname(UUID id){
        String newNickname = inputHandler.getNewInput();
        // id 로 찾아와서, 레포지토리의 load하는 메서드를 거쳐서
        // 역직렬화를 시킨 후 set으로 변경한다.

        //System.out.println("0            " + id);

        User user = getUserById(id);
        //System.out.println("1            " + user.getId());
        user.setNickname(newNickname);
        //System.out.println("2            " +user.getId());

        // 그리고 변경된 사실을 덮어쓰기
        fileUserRepository.saveUser(user);
        //System.out.println("5             " +id);

        User updatedUser = fileUserRepository.findUserById(id).orElse(null);
        System.out.println("Updated User: " + updatedUser);
    }


    // Delete : 전체 유저 삭제, 특정 유저 삭제
    public void clearAllUsers(){
        fileUserRepository.deleteAllUsers();
    }
    public void removeUserById(UUID id){
        fileUserRepository.deleteUserById(id);
    }
}
