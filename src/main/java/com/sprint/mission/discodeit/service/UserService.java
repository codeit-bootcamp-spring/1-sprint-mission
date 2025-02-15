package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserCreateDTO userCreateDTO);
    User findById(UUID userId);
    List<User> findAll();
    User update(UUID userId, UserUpdateDTO userUpdateDTO);
    void delete (UUID userId);

    //중복 유효성 검사
    boolean checkNameDuplicate(String userName);
    boolean checkEmailDuplicate(String userEmail);
}
