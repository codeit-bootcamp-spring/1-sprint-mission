package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ChannelDto;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void createNewUser(String name,String password,String email);
    <T> List<User> readUser(T user);
    List<User> readUserAll();
    boolean updateUserName(String name,String change);
    boolean updateUserName(UUID id,String changeName);
    boolean deleteUser(UUID id,String password);
    boolean deleteUser(String name,String password);
    <T> void updateUserStatus(T user);

    boolean updateUserSelfImg(String name,char [] img);
    boolean updateUserSelfImg(UUID id,char [] img);
    <T,C,K> boolean sendMessageToUser(T sender,C reciver, K message);



}

