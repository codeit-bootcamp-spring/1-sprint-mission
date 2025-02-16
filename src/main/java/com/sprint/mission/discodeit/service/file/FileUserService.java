package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.form.CheckUserDto;
import com.sprint.mission.discodeit.entity.form.UserUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRespository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class FileUserService implements UserService {

    private final UserRepository fileUserRespository;

    public FileUserService(UserRepository fileUserRespository) {
        this.fileUserRespository = fileUserRespository;
    }

    @Override
    public void createUser(User user) {
        fileUserRespository.createUser(user.getId(),user);
    }

    @Override
    public Optional<CheckUserDto> findUser(UUID id) {
        return fileUserRespository.findById(id).map(CheckUserDto::new);
    }

    @Override
    public Optional<CheckUserDto> findByloginId(String loginId) {
        return fileUserRespository.findByloginId(loginId).map(CheckUserDto::new);
    }

    @Override
    public List<User> findAllUsers() {
       return fileUserRespository.findAll();
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        validateFileUserExits(id);
        fileUserRespository.updateUser(id,userParam);
    }

    @Override
    public void deleteUser(UUID id) {
        validateFileUserExits(id);
        fileUserRespository.deleteUser(id);
    }
    private void validateFileUserExits(UUID uuid) {
        if (!fileUserRespository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
