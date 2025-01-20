package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.dto.UserDTO;
import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.UserStatus;
import com.sprint.misson.discordeit.service.UserService;

import java.util.List;

public class FileUserService implements UserService {
    @Override
    public User createUser(String nickname, String email) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User getUserByUUID(String userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getUsersByNickname(String nickname) {
        return List.of();
    }

    @Override
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus) {
        return List.of();
    }

    @Override
    public List<User> getUserByUserStatus(UserStatus userStatus) {
        return List.of();
    }

    @Override
    public User updateUser(String userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public boolean deleteUser(String userId) {
        return false;
    }
}
