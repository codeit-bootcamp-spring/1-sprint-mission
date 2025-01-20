package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.UserDTO;
import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.UserStatus;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final HashMap<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User createUser(String nickname, String email) throws CustomException {

        //스트림으로 HashMap 의 value(User) 들 중 이미 존재하는 email 인지 검사
        boolean userEmailExists = data.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));

        if (userEmailExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        User newUser = new User(nickname, email, UserStatus.ACTIVE, null, AccountStatus.UNVERIFIED);

        //TODO - 추가 구현할만 한 기능
        //이메일 인증
        //1. 이메일 발송
        //2. 사용자가 이메일에서 인증 버튼 클릭 or 인증 코드 입력
        //3. 인증 완료 시, user.accountStatus 를 VERIFIED 로 변경

        //* put() - 이미 해당 key 에 key-value 셋이 존재했다면 이전 value 반환(value 가 null 이었으면 null 반환)
        //          해당하는 key-value 셋이 없었다면 null 반환
        data.put(newUser.getId(), newUser);

        return newUser;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User getUserByUUID(String userId) throws CustomException {

        User user = data.get(UUID.fromString(userId));

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws CustomException {

        User user = data.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> getUsersByNickname(String nickname) {
        return data.values().stream()
                .filter(u -> u.getNickname().contains(nickname))
                .toList();
    }

    @Override
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus) {
        return data.values().stream()
                .filter(u -> u.getAccountStatus().equals(accountStatus))
                .toList();
    }

    @Override
    public List<User> getUserByUserStatus(UserStatus userStatus) {
        return data.values().stream()
                .filter(u -> u.getUserStatus().equals(userStatus))
                .toList();
    }

    @Override
    public User updateUser(String userId, UserDTO userDTO) throws CustomException {
        User user = data.get(UUID.fromString(userId));

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
            user.setUpdatedAt();
        }
        return user;
    }

    @Override
    public boolean deleteUser(String userId) throws CustomException {

        User user = data.get(UUID.fromString(userId));

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }

        return data.remove(UUID.fromString(userId), user);
    }
}
