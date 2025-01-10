package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserSerivce implements UserService {
    private final Map<UUID, User> userRepository;

    public JCFUserSerivce() {
        this.userRepository = new HashMap<>();
    }

    @Override
    public MyLog<User> createUser(String name, String phone, String password) {
        if (!User.isValidPassword(password)) {
            return new MyLog<>(null, "올바르지 않은 비밀번호 형식 입니다. 비밀번호는 8자리 이상 15자리 이하 대문자 및 특수문자 하나 이상 포함해야 합니다.");
        }

        if (!User.isValidPhone(phone)) {
            return new MyLog<>(null, "올바르지 않은 핸드폰 번호 형식 입니다");
        }

        for (User user : userRepository.values()) {
            if (user.getPhone().equals(phone)) {
                return new MyLog<>(null, "이미 존재하는 아이디 입니다");
            }
        }
        User createUser = new User(name, phone, password);
        userRepository.put(createUser.getId(), createUser);
        return new MyLog<>(createUser, " 저장이 완료되었습니다");
    }

    @Override
    public MyLog<User> getUserById(String phone) {
        for (User user : userRepository.values()) {
            if (user.getPhone() == phone) {
                return new MyLog<>(user, "해당 아이디의 유저를 발견하였습니다");
            }
        }
        return new MyLog<>(null, "해당 아이디를 가진 유저가 존재하지 않습니다");
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userRepository.values());
    }

    @Override
    public MyLog<User> updateUserPassword(String phone, String newPass) {
        for (User user : userRepository.values()) {
            if (user.getPhone() == phone) {
                user.update(newPass);
                return new MyLog<>(user, " 비밀번호 변경이 완료되었습니다");
            }
        }
        return new MyLog<>(null, "입력하신 아이디에 해당하는 유저가 존재하지 않습니다");
    }

    @Override
    public MyLog<User> deleteUser(User user) {
        if (userRepository.get(user.getId()) != null) {
            userRepository.remove(user.getId());
            return new MyLog<>(user, " 삭제를 완료하였습니다");
        }
        return new MyLog<>(null, "존재하지 않는 사용자입니다");
    }
}
