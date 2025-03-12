package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserDto dto, byte[] profileImage) {

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .online(dto.isOnline())
                .build();

        if (profileImage != null && profileImage.length > 0) {
            user.setProfileImage(profileImage);
        }
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public UsersDto update(UUID id, UsersDto usersDTO, byte[] profileImage) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Objects.requireNonNull(usersDTO, "UserDTO cannot be null");

        userRepository.save(user);

        if (profileImage != null && profileImage.length > 0) {
            String fileName = "profile_" + user.getId();
            Long size = (long) profileImage.length;
            String contentType = "image/jpeg";
            BinaryContent newProfile = new BinaryContent(fileName, size, contentType);
            binaryContentRepository.save(newProfile);
        }

        return usersDTO;
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
        if (binaryContentRepository.existsById(id)) {
            binaryContentRepository.deleteById(id);
        }
    }

    @Override
    public UserDto find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public List<UsersDto> findAll() {
        try {
            List<User> users = userRepository.findAll();

            if (users == null) {
                log.warn("경고: userRepository.findAll()이 null을 반환했습니다.");
                return new ArrayList<>();
            }

            return users.stream()
                    .map(userMapper::toDtos)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생: " + e.getMessage());
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return new ArrayList<>();
        }
    }

    public void updateOnlineStatus(UUID userId, boolean online) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setOnline(online);

        userRepository.save(user);
    }
}