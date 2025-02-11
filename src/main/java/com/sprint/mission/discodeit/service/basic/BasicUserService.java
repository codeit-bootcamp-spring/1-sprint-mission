package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(BasicUserService.class);
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("유저를 생성합니다: {}", userDto.getUsername());

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다");
        }

        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "",
                null
        );
    }

    @Override
    public UserDto findUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 유저를 찾을 수 없습니다: " + userId));
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "",
                null
        );
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        "",
                        null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 유저를 찾을 수 없습니다: " + userId));

        user.update(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "",
                null
        );
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public AuthDto.LoginResponse login(AuthDto.LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .filter(user -> user.getPassword().equals(loginRequest.getPassword()))
                .map(user -> new AuthDto.LoginResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다"));
    }
}
