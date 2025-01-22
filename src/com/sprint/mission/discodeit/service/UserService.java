package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    public void createUser(User user);
    User readUser(String userId);
    List<User> readAllUser();
    public void modifyUser(String userID, String newName);
    public void deleteUser(String userID);

    public void addMessage(User user, Message message); //유저-메시지 추가
}
