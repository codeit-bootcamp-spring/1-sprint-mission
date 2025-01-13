package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface UserService {
    void CreateUserDefault(String name);
    <T> String ReadUser(T user);
    String ReadUserAll();
    boolean UpdateUserName(String name,String change);
    boolean UpdateUserName(UUID id,String changeName);
    boolean DeleteUser(UUID id);
    boolean DeleteUser(String name);





}
