package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.HashMap;
import java.util.UUID;

public interface UserRepository {
     User getUserById(UUID userId) throws Exception;
     HashMap<UUID, User> getUsersMap() throws Exception;
     boolean deleteUser(UUID userId) throws Exception;
     boolean saveUser(User user) throws Exception;
     boolean isUserExistenceByUUID(UUID userId) throws Exception;
     boolean isUserExistByName(String userName) throws Exception;
     boolean isUserExistByEmail(String email) throws Exception;
     boolean validateUserToLogin(String userName, String password) throws Exception;
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
