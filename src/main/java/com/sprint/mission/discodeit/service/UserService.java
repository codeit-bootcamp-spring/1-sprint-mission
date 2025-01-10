package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/*
 *  CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 구현한 인터페이스
 * */

public interface UserService {
    void addUser(User user);
    User getUser(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, String newUsername, String newPassword);
    void deleteUser(UUID id);
}
