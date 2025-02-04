package com.sprint.misson.discordeit.service.basic;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.UserDTO;
import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.UserStatus;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.repository.UserRepository;
import com.sprint.misson.discordeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String nickname, String email, String password) {
        boolean userExists = userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
        if (userExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }
        User newUser = new User(nickname, email, password, UserStatus.ACTIVE, null, AccountStatus.UNVERIFIED);
        return userRepository.save(newUser);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUUID(String userId) throws CustomException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws CustomException {
        User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> getUsersByNickname(String nickname) {
        return userRepository.findAll().stream().filter(user -> user.getNickname().contains(nickname)).toList();
    }

    @Override
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus) {
        return userRepository.findAll().stream().filter(user -> user.getAccountStatus().equals(accountStatus)).toList();
    }

    @Override
    public List<User> getUserByUserStatus(UserStatus userStatus) {
        return userRepository.findAll().stream().filter(user -> user.getUserStatus().equals(userStatus)).toList();
    }

    @Override
    public User updateUser(String userId, UserDTO userDTO, long updatedAt) throws CustomException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        } else if (userDTO == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
        }

        boolean isUpdated = false;

        if (!user.getNickname().equals(userDTO.getNickname()) && userDTO.getNickname() != null && !userDTO.getNickname().isEmpty()) {
            user.setNickname(userDTO.getNickname());
            isUpdated = true;
        }
        if (!user.getEmail().equals(userDTO.getEmail()) && userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
            isUpdated = true;
        }
        if (!user.getUserStatus().equals(userDTO.getUserStatus()) && userDTO.getUserStatus() != null) {
            user.setUserStatus(userDTO.getUserStatus());
            isUpdated = true;
        }
        if (!user.getAccountStatus().equals(userDTO.getAccountStatus()) && userDTO.getAccountStatus() != null) {
            user.setAccountStatus(userDTO.getAccountStatus());
            isUpdated = true;
        }

        if (isUpdated) {
            user.setUpdatedAt(updatedAt);
        }

        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(String userId) throws CustomException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return userRepository.delete(user);
    }
}
