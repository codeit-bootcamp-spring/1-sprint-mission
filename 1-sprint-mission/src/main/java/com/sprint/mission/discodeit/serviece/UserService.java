package com.sprint.mission.discodeit.serviece;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.UUID;

public interface UserService {
    void CreateUser(String name);
    String ReadUser(String name);
    String ReadUser(UUID name);
    String ReadUserAll();
    void UpdateUserName(String name,String change);
    void UpdateUserName(UUID id,String changeName);
    void DeleteUser(UUID id);
    void DeleteUser(String name);





}
