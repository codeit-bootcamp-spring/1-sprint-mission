package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.UserService;

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
        boolean userEmailExists = findAll().stream().anyMatch(u -> u.getEmail().equals(email));

        if (userEmailExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        UserStatus userStatus = new UserStatus();
        User newUser = new User(nickname, email, password, userStatus.getId(), null, AccountStatus.UNVERIFIED);

        Path newUserPath = userDirectory.resolve(newUser.getId().concat(".ser"));
        save(newUserPath, newUser);

        return newUser;
    }

    @Override
    public List<User> findAll() {
        return load(userDirectory);
    }

    @Override
    public User findById(String userId) throws CustomException {
        User user = findAll().stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws CustomException {
        User user = findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> findAllContainsNickname(String nickname) {
        return findAll().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .toList();
    }

    @Override
    public List<User> findAllByAccountStatus(AccountStatus accountStatus) {
        return findAll().stream()
                .filter(u -> u.getAccountStatus().equals(accountStatus))
                .toList();
    }

//    @Override
//    public List<User> getUserByUserStatus(UserStatus userStatus) {
//        return getUsers().stream()
//                .filter(u -> u.().equals(userStatus))
//                .toList();
//    }

    @Override
    public User updateUser(String userId, UpdateUserDto updateUserDto) throws CustomException {
        User user = findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        } else if (updateUserDto == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
        }

        if (user.isUpdated(updateUserDto)) {
            user.setUpdatedAt(updateUserDto.updatedAt());
            Path userPath = userDirectory.resolve(user.getId().concat(".ser"));
            save(userPath, user);
        }

        return user;
    }

    @Override
    public boolean deleteUser(String userId) {
        User user = findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }

        return delete(userDirectory.resolve(user.getId()));
    }
}
