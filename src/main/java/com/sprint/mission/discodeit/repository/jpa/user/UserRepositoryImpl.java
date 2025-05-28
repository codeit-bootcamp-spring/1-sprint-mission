package com.sprint.mission.discodeit.repository.jpa.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void removeUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsUser(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsUser(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public Optional<User> findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findUserByNameAndPassword(String name, String password) {
        return userRepository.findUserByNameAndPassword(name, password);
    }
}
