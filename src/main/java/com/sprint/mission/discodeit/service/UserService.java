package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID createUser(String userName, String email,String password);

    UUID createUser(String userName, String email,String password, String profilePicturePath);

    UserDto findUserById(UUID id);

    List<UserDto> findAllUsers();

    boolean deleteUser(UUID userId);

    boolean changeUserName(UUID userId, String newName);

    boolean changeProfilePicture(UUID userId, String profilePicturePath);

    boolean deleteProfilePicture(UUID userId);

}

