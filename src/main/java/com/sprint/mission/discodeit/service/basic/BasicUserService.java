package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //서비스 로직
    @Override
    public UUID create(String username, String email, String password) {
        if(!User.validation(username, email, password)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    @Override
    public User read(UUID id) {
        User findUser = userRepository.findOne(id);
        return Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UUID id, String name, String email){
        User findUser = userRepository.findOne(id);
        findUser.setUser(name, email);
        userRepository.update(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = userRepository.findOne(id);
        return userRepository.delete(findUser.getId());
    }
}
