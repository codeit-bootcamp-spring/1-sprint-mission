package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.HashMap;
import java.util.UUID;

public interface UserRepository {
    public User getUser(UUID userId);
    public HashMap<UUID, User> getUsersMap();
    public boolean deleteUser(UUID userId);
    public boolean addUser(User user);
    public boolean isUserExist(UUID userId);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
