package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.HashMap;
import java.util.UUID;

public interface UserRepository {
     User getUserById(UUID userId) ;
     HashMap<UUID, User> getUsersMap() ;
     boolean deleteUser(UUID userId) ;
     boolean saveUser(User user) ;
     boolean isUserExistByUUID(UUID userId);
     boolean isUserExistByName(String userName) ;
     boolean isUserExistByEmail(String email) ;
     boolean validateUserToLogin(String userName, String password) ;
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
