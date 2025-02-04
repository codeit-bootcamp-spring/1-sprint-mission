package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User register(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserDetails(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean updateUserProfile(UUID id, String name, String email, String password, UserStatus status) {
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            User user = byId.get();
            user.update(name, email, password, status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return userRepository.delete(id);
    }
}
