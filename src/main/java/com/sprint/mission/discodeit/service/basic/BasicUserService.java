package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserIdDTO;
import com.sprint.mission.discodeit.dto.user.CreatedUserDataDTO;
import com.sprint.mission.discodeit.dto.ProfileImageDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
    private final UserStatusService basicUserStatusService;
    private final BinaryContentService binaryContentService;

//    public BasicUserService(FileUserRepository fileUserRepository,UserValidator userValidator) {
//        this.userValidator = userValidator;
//        this.fileUserRepository = fileUserRepository;
//    }

    @Override
    public User createUser(String name, String email, String iD , String password) {
        try{
            userValidator.validateUser(name, email, password);
        }catch (CustomException e){
            System.out.println("유저생성 실패 ->" + e.getMessage());
        }
            User user = new User(name, email, iD, password);
            fileUserRepository.save(user);
            return user;

    }

    public User createUser(CreatedUserDataDTO data, ProfileImageDTO proFile) {
        try {
            userValidator.validateUser(data.name(), data.email(), data.password());
        }catch (CustomException e){
            System.out.println("유저생성 실패 -> " + e.getMessage());
        }
        User user = new User(data.name(), data.email(), data.id(), data.password());
        fileUserRepository.save(user);

        if(proFile != null){
            binaryContentService.created(proFile);
        }
        // 유저생성하고 UserStatus를 같이 생성합니다.
        UserIdDTO statusDTO = new UserIdDTO(user.getId());
        basicUserStatusService.created(statusDTO);
        return user;

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
    //추가적으로 유저 객체를 삭제시 해당되는 UserStatus 객체로 삭제하도록
    }
}
