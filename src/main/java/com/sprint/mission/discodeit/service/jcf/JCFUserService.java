package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final HashMap<String, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(String nickname, String email, String password) throws CustomException {

        //스트림으로 HashMap 의 value(User) 들 중 이미 존재하는 email 인지 검사
        boolean userEmailExists = data.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));

        if (userEmailExists) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }

        UserStatus userStatus = new UserStatus();
        User newUser = new User(nickname, email, password, userStatus.getId(), null, AccountStatus.UNVERIFIED);

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
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public User findById(String userId) throws CustomException {

        User user = data.get(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws CustomException {

        User user = data.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with email %s not found", email));
        }
        return user;
    }

    @Override
    public List<User> findAllContainsNickname(String nickname) {
        return data.values().stream()
                .filter(u -> u.getNickname().contains(nickname))
                .toList();
    }

    @Override
    public List<User> findAllByAccountStatus(AccountStatus accountStatus) {
        return data.values().stream()
                .filter(u -> u.getAccountStatus().equals(accountStatus))
                .toList();
    }

    // UserStatus 변경으로 인한 임시 주석 처리
//    @Override
//    public List<User> getUserByUserStatus(UserStatus userStatus) {
//        return data.values().stream()
//                .filter(u -> u.getUserStatusId().equals(userStatus))
//                .toList();
//    }

    @Override
    public User updateUser(String userId, UpdateUserDto updateUserDto) throws CustomException {
        User user = data.get(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, String.format("User with id %s not found", userId));
        }
        if (user.isUpdated(updateUserDto)) {
            user.setUpdatedAt(updateUserDto.updatedAt());
        }
        return user;
    }

    @Override
    public boolean deleteUser(String userId) throws CustomException {
        return data.remove(userId) != null;
    }
}
