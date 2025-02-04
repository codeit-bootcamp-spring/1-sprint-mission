package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private FileMessageService fileMessageService;
    private final FileUserRepository fileUserRepository;
    private static volatile FileUserService instance;

    private FileUserService() {
        this.fileUserRepository = new FileUserRepository();
    }

    public void setFileMessageService(FileMessageService fileMessageService) {
        this.fileMessageService = fileMessageService;
    }
    public static FileUserService getInstance() {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public UUID createUser(String userName) {
        return fileUserRepository.save(userName);
    }

    @Override
    public User getUser(UUID id) {
        return fileUserRepository.findUserById(id);
    }

    @Override
    public List<User> getUsers() {
        List<User> collect = fileUserRepository.findAll();
        return new ArrayList<>(collect);
    }

    @Override
    public void updateUser(UUID id, String username) {
        fileUserRepository.update(id, username);
        //리턴 생각해보기

    }

    @Override
    public void deleteUser(UUID id) {
        List<User> users = fileUserRepository.findAll();
        if(users.stream().map(User::getId).toList().contains(id)){
            System.out.println("유저 삭제하기 전 유저가 쓴 메시지 객체 찾기");
            List<Message> messagesByUserId = fileMessageService.getMessagesByUserId(id);

            boolean delete = fileUserRepository.delete(id);
            if(delete){
                System.out.println("messagesByUserId = " + messagesByUserId);
                for (Message message : messagesByUserId) {
                    fileMessageService.deleteMessage(message.getId());
                } 
            }else {
                System.out.println("유저 삭제에 실패했습니다.");
                
            }
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

}
