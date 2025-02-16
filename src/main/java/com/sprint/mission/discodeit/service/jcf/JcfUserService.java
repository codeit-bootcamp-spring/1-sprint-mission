package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.form.CheckUserDto;
import com.sprint.mission.discodeit.entity.form.UserUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JcfUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(User user) {
        if (user.getUserName().trim().isEmpty()) {
            log.info("이름을 입력해주세요.");
            return;
        }
        if (user.getUserEmail().trim().isEmpty()) {
            log.info("이메일을 입력해주세요.");
            return;
        }
        userRepository.createUser(user.getId(), user);
    }

    @Override
    public Optional<CheckUserDto> findUser(UUID id) {
        return userRepository.findById(id).map(CheckUserDto::new);
    }

    @Override
    public Optional<CheckUserDto> findByloginId(String loginId) {
        return userRepository.findByloginId(loginId).map(CheckUserDto::new);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        validateUserExits(id);
        userRepository.updateUser(id, userParam);
    }

    @Override
    public void deleteUser(UUID id) {
        validateUserExits(id);
        userRepository.deleteUser(id);
    }
    private void validateUserExits(UUID uuid) {
        if (!userRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

}
