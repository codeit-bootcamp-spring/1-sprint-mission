package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface UserService {
    void createNewUser(String name);
    <T> String readUser(T user);
    String readUserAll();
    boolean updateUserName(String name,String change);
    boolean updateUserName(UUID id,String changeName);
    boolean deleteUser(UUID id);
    boolean deleteUser(String name);





}
