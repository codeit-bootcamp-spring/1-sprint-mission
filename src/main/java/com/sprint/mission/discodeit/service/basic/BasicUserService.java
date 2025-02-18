package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUserStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public User createUser(UserCreateDTO userCreateDTO) {
        if (isNameExist(userCreateDTO.name())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다. ");
        }
        if (isEmailExist(userCreateDTO.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. ");
        }
        User user = new User(userCreateDTO);

        userStatusService.saveExist(user.getUserStatus());
        userRepository.save(user);
        return user;
    }

    @Override
    public UserFindDTO findUserDTO(UUID userId) {
        User user = userRepository.findbyId(userId);
        UserFindDTO userFindDTO = new UserFindDTO(user);
        return userFindDTO;
    }

    //내부 사용전용
    private User findbyId(UUID userId) {
        User user = userRepository.findbyId(userId);
        return user;
    }

    private List<User> findAll(){
        return new ArrayList<>(userRepository.load().values());
    }

    @Override
    public List<UserFindDTO> findAllUserDTO() {
        List<User> userList = findAll();
        List<UserFindDTO> userFindDTOS = userList.stream()
                .map(user-> new UserFindDTO(user))
                .collect(Collectors.toList());
        return userFindDTOS;
    }

    @Override
    public User updateUser(UUID userID, UserUpdateDTO userUpdateDTO) {
        User user = findbyId(userID);
        user.updateUser(userUpdateDTO);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userID) {
        User user = findbyId(userID);
        user.deleteUserStatus();
        user.deleteBinaryContent();
        userRepository.delete(userID);
    }

    @Override
    public UserUserStatusUpdateDTO updateUserStatus(UUID id, UserUserStatusUpdateDTO userUserStatusUpdateDTO) {
        UserStatusUpdateDTO userStatusUpdateDTO = new UserStatusUpdateDTO(userUserStatusUpdateDTO.time());
        userStatusService.updateByUserId(id, userStatusUpdateDTO);
        return userUserStatusUpdateDTO;
    }

    @Override
    public Boolean isNameExist(String name) {
        return userRepository.load().values()
                .stream()
                .anyMatch(user -> user.getUserName().equals(name));
    }

    @Override
    public Boolean isEmailExist(String email) {
        return userRepository.load().values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
