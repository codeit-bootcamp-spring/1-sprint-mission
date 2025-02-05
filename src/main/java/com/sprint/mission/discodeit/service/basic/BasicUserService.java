package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;


@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public User create(String nickname, String email, String password) {
        boolean userExists = userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
        if (userExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        UserStatus userStatus = new UserStatus();
        User newUser = new User(nickname, email, password, userStatus.getId(), null, AccountStatus.UNVERIFIED);
        return userRepository.save(newUser);
    }

    public User create(String nickname, String email, String password, BinaryContent profileImage) {
        User user = create(nickname, email, password);
        UserStatus userStatus = new UserStatus();
        user.setUserStatus(userStatus.getId());
        return user;
    }

    @Override
    public List<UserDTO> getUsers() {
        for (User u : userRepository.findAll()) {
            UserDTO userDTO = new UserDTO(u.getId(), u.getNickname(), u.getEmail(),
                    userStatusService.findById(u.getUserStatusId()).isActive(), u.getStatusMessage(), u.getAccountStatus(), u.getProfileImageId());
        }

        return null;
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

    //UserStatus 필드 변경으로 인한 임시 주석 처리
//    @Override
//    public List<User> getUserByUserStatus(UserStatus userStatus) {
//        return userRepository.findAll().stream().filter(user -> user.getUserStatus().equals(userStatus)).toList();
//    }

    @Override
    public User updateUser(String userId, UserDTO userDTO, Instant updatedAt) throws CustomException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        } else if (userDTO == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
        }

        if (user.isUpdated(userDTO)) {
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
