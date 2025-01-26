package com.srint.mission.discodeit.service.basic;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.UserRepository;
import com.srint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //서비스 로직
    @Override
    public UUID create(String username, String email, String password) {
        validateDuplicateUser(email);
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    @Override
    public User read(UUID id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserName(UUID id, String name) {
        User findUser = userRepository.findOne(id);
        findUser.setUsername(name);
        userRepository.update(findUser);
        return findUser;
    }

    @Override
    public User updateEmail(UUID id, String email) {
        User findUser = userRepository.findOne(id);
        findUser.setEmail(email);
        userRepository.update(findUser);
        return findUser;
    }

    @Override
    public User updatePassword(UUID id, String password) {
        User findUser = userRepository.findOne(id);
        findUser.setPassword(password);
        userRepository.update(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = userRepository.findOne(id);
        if(!findUser.getMyChannels().isEmpty()){
            findUser.getMyChannels().forEach(
                    channel -> channel.deleteJoinedUser(findUser));
        }
        return userRepository.delete(findUser.getId());

    }

    private void validateDuplicateUser(String email) {
        if(!userRepository.findAll().isEmpty()){ //여기 로직 주의
            List<User> users = userRepository.findAll();
            if (users.stream().anyMatch(user-> email.equals((user.getEmail())))){
                throw new IllegalStateException("이미 존재하는 이메일 입니다.");
            }
        }
    }
}
