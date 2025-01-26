package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.UserDTO;
import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.UserStatus;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.UserService;

import java.nio.file.Path;
import java.util.List;

public class FileUserService extends FileService implements UserService {

    private final Path userDirectory;


    public FileUserService() {
        this.userDirectory= super.getBaseDirectory().resolve("user");
        init(userDirectory);
    }

    @Override
    public User create(String nickname, String email, String password) {

        //스트림으로 HashMap 의 value(User) 들 중 이미 존재하는 email 인지 검사
        boolean userEmailExists = getUsers().stream().anyMatch(u -> u.getEmail().equals(email));

        if (userEmailExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        User newUser = new User(nickname, email, password, UserStatus.ACTIVE, null, AccountStatus.UNVERIFIED);

        Path newUserPath = userDirectory.resolve(newUser.getId().concat(".ser"));
        save(newUserPath, newUser);

        return newUser;
    }

    @Override
    public List<User> getUsers() {
        return load(userDirectory);
    }

    @Override
    public User getUserByUUID(String userId) throws CustomException {
        User user = getUsers().stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws CustomException {
        User user = getUsers().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> getUsersByNickname(String nickname) {
        return getUsers().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .toList();
    }

    @Override
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus) {
        return getUsers().stream()
                .filter(u -> u.getAccountStatus().equals(accountStatus))
                .toList();
    }

    @Override
    public List<User> getUserByUserStatus(UserStatus userStatus) {
        return getUsers().stream()
                .filter(u -> u.getUserStatus().equals(userStatus))
                .toList();
    }

    @Override
    public User updateUser(String userId, UserDTO userDTO, long updatedAt) throws CustomException {
        User user = getUserByUUID(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        } else if (userDTO == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
        }

        boolean isUpdated = false;

        if (!user.getNickname().equals(userDTO.getNickname())
                && userDTO.getNickname() != null
                && !userDTO.getNickname().isEmpty()) {
            user.setNickname(userDTO.getNickname());
            isUpdated = true;
        }
        if (!user.getEmail().equals(userDTO.getEmail())
                && userDTO.getEmail() != null
                && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
            isUpdated = true;
        }
        if (!user.getUserStatus().equals(userDTO.getUserStatus())
                && userDTO.getUserStatus() != null) {
            user.setUserStatus(userDTO.getUserStatus());
            isUpdated = true;
        }
        if (!user.getAccountStatus().equals(userDTO.getAccountStatus())
                && userDTO.getAccountStatus() != null) {
            user.setAccountStatus(userDTO.getAccountStatus());
            isUpdated = true;
        }

        if (isUpdated) {
            user.setUpdatedAt(updatedAt);
            //todo - 고민
            //덮어씌우게 될까?
            Path userPath = userDirectory.resolve(user.getId().concat(".ser"));
            save(userPath, user);
        }

        return user;
    }

    @Override
    public boolean deleteUser(String userId) {
        User user = getUserByUUID(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }

        return delete(userDirectory.resolve(user.getId()));
    }
}
