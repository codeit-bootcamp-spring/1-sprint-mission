package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRespository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class FileUserService implements UserService {

    private final UserRepository fileUserRespository=new FileUserRespository();

    @Override
    public void createUser(User user) {
        fileUserRespository.createUser(user.getId(),user);
    }

    @Override
    public Optional<User> findUser(UUID id) {
        return fileUserRespository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
       return fileUserRespository.findAll();
    }

    @Override
    public void updateUserName(UUID id, String userName) {
        if(fileUserRespository.findById(id).isPresent()) {
            fileUserRespository.updateUserName(id,userName);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void updateUserEmail(UUID id, String userEmail) {
        if(fileUserRespository.findById(id).isPresent()) {
            fileUserRespository.updateUserEmail(id,userEmail);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(fileUserRespository.findById(id).isPresent()) {
            fileUserRespository.deleteUser(id);
        }else{
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
