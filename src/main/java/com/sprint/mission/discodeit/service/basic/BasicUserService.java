package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserValidator userValidator;
    private final FileUserRepository fileUserRepository;

//    public BasicUserService(FileUserRepository fileUserRepository,UserValidator userValidator) {
//        this.userValidator = userValidator;
//        this.fileUserRepository = fileUserRepository;
//    }

    @Override
    public User CreateUser(String name, String email, String iD , String password) {
        if (userValidator.validateUser(name, email, password, fileUserRepository.findAll())) {
            User user = new User(name, email, iD, password);
            fileUserRepository.save(user);
            return user;
        }
        throw new CustomException(ExceptionText.USER_CREATION_FAILED);
    }


    @Override
    public User getUser(UUID uuid) {
        return fileUserRepository.findById(uuid);
    }

    @Override
    public HashMap<UUID, User> getAllUsers() {
        return fileUserRepository.findAll();
    }

    @Override
    public void updateUser(UUID uuid, String email, String id, String password) {
        User user = fileUserRepository.findById(uuid);
        if (user != null) {
            user.update(email, id, password);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        fileUserRepository.delete(uuid);
    }
}
