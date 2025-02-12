package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto getUserById(UUID id);

    UUID createUser(String userName, String email,String password);

    //아직 바이너리콘텐트레포지토리 구현안돼있어서 주석처리함.(미션3에서 지금 구현하지 않기를 요구)
    //UUID createUser(String userName, String email,String password, String profilePicturePath);

    String getUserNameById(UUID id);

    boolean deleteUser(UUID userId);

    boolean changeUserName(UUID userId, String newName);

    //아직 바이너리콘텐트레포지토리 구현안돼있어서 주석처리함.(미션3에서 지금 구현하지 않기를 요구)
    //boolean changeProfilePicture(UUID userId, String profilePicturePath);

    boolean deleteProfilePicture(UUID userId);

}

