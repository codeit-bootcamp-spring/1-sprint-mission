package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.dto.entity.UserStatus;
import com.sprint.mission.discodeit.dto.form.CheckUserDto;
import com.sprint.mission.discodeit.dto.form.UserUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfParticipantRepository;
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
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final JcfParticipantRepository participantRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void createUser(User user) {
        List<User> all = userRepository.findAll();
        for (User findUser : all) {
            if (user.getUserName().equals(findUser.getUserName())) {
                log.info("중복된 이름입니다.");
                return;
            }
            if (user.getUserEmail().equals(findUser.getUserEmail())) {
                log.info("중복된 이메일입니다.");
                return;
            }
        }
        userRepository.createUser(user.getId(), user);
        UserStatus userStatus = new UserStatus(user.getId());
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
