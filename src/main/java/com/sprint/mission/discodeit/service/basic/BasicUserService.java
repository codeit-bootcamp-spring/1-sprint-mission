package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserBaseDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserBaseDTO createUser(UserCreateDTO userCreateDTO) {
        if(checkEmailDuplicate(userCreateDTO.getUserEmail())){
            throw new IllegalArgumentException(userCreateDTO.getUserEmail() + "이미 가입한 적이 있는 email 입니다.");}
        if(checkNameDuplicate(userCreateDTO.getUserName())) {
            throw new IllegalArgumentException(userCreateDTO.getUserName() + "이미 존재하는 닉네임입니다."); }
        User user = new User(userCreateDTO);
        userRepository.save(user);
        return new UserBaseDTO(user.getId(), user.getUserName(), user.getUserEmail());
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("UserId :" + userId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> data = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        if(data.isEmpty()) return userList;
        data.values().stream().sorted(Comparator.comparing(user -> user.getCreatedAt())).forEach(user -> {
            userList.add(user);
        });
        return userList;
    }

    @Override
    public User update(UUID userId, UserUpdateDTO userUpdateDTO) {
        if(checkEmailDuplicate(userUpdateDTO.getUserEmail())) {
            throw new IllegalArgumentException(userUpdateDTO.getUserEmail() + "이미 가입되어 있는 이메일 입니다.");
        }
        if(checkNameDuplicate(userUpdateDTO.getUserName())) {
            throw new IllegalArgumentException(userUpdateDTO.getUserName() + "이미 존재하는 닉네임입니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("UserId : " + userId + "를 찾을 수 없습니다."));
        user.update(userUpdateDTO);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if(userRepository.existsById(userId)){
            userRepository.delete(userId);
        } else {
            throw new NoSuchElementException("UserId : " + userId + "를 찾을 수 없습니다.");
        }

    }

    @Override
    public boolean checkNameDuplicate(String userName) {
        Map<UUID, User> userMap = userRepository.findAll();
        if(userMap == null || userMap.isEmpty()) return false;
        return userMap.values().stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean checkEmailDuplicate(String userEmail) {
        Map<UUID, User> userMap = userRepository.findAll();
        if (userMap == null || userMap.isEmpty()) return false;
        return userMap.values().stream().anyMatch(user -> user.getUserEmail().equals(userEmail));
    }

}
