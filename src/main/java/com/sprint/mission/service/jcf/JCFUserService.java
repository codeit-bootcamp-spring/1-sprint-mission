package com.sprint.mission.service.jcf;


import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.JCFUserRepository;
import com.sprint.mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    // ChannelRepository 인터페이스로 안 받은 이유 : file클래스들로 인해 생긴 ioe예외 받기 싫어서
    private final JCFUserRepository userRepository = new JCFUserRepository();

    public static JCFUserService userService;
    private JCFUserService() {}
    public static JCFUserService getInstance(){
        if (userService == null) return userService = new JCFUserService();
        else return userService;
    }

    @Override // 닉네임 중복 허용
    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return createOrUpdate(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);  // null 위험 없음
    }

    @Override
    public Set<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
        user.getChannelsImmutable().stream()
                        .forEach(channel -> channel.removeUser(user));
        userRepository.delete(user);
    }
//
//    @Override 닉네임 중복 허용 안할 시
//    public void validateDuplicateName(String name) {
//
//        if (!findUsersByName(name).isEmpty()) {
//            throw new DuplicateName(String.format("%s(은)는 이미 존재하는 닉네임입니다", name));
//        }
//    }
}
