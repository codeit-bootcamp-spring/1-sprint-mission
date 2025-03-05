package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UsersDto create(UsersDto dto, byte[] profileImage) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setOnline(true);
        if (profileImage != null && profileImage.length > 0) {
            user.setProfileImage(profileImage);
        }
        User saved = userRepository.save(user);
        return convertToDTO(saved);  // User 엔티티를 UsersDTO로 변환해서 반환
    }

    private UsersDto convertToDTO(User user) {
        UsersDto dto = new UsersDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setOnline(user.isOnline());
        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            dto.setProfileImage(Base64.getEncoder().encodeToString(user.getProfileImage()));
        } else {
            dto.setProfileImage("");
        }
        return dto;
    }

    @Transactional
    @Override
    public UsersDto update(String id, UsersDto usersDTO, byte[] profileImage) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Objects.requireNonNull(usersDTO, "UserDTO cannot be null");

        userRepository.save(user);

        if (profileImage != null && profileImage.length > 0) {
            String fileName = "profile_" + user.getId();
            Long size = (long) profileImage.length;
            String contentType = "image/jpeg";
            BinaryContent newProfile = new BinaryContent(fileName, size, contentType, profileImage);
            binaryContentRepository.save(newProfile);
        }

        return usersDTO;
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
        UUID userUUID = UUID.fromString(id);
        if (binaryContentRepository.existsById(userUUID)) {
            binaryContentRepository.deleteById(userUUID);
        }
    }

    @Override
    public UserDto find(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    private UsersDto toDTO(User user) {
        UsersDto dto = new UsersDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setOnline(user.isOnline());

        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            String base64Str = Base64.getEncoder().encodeToString(user.getProfileImage());
            dto.setProfileImage(base64Str);
        } else {
            dto.setProfileImage("");
        }
        return dto;
    }

    @Override
    public List<UsersDto> findAll() {
        try {
            List<UsersDto> users = userRepository.findAll();
            if (users == null) {
               log.warn("경고: userRepository.findAll()이 null을 반환했습니다.");
                return new ArrayList<>();
            }
            return users;
        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateOnlineStatus(String userId, boolean online) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setOnline(online);

        userRepository.save(user);
    }
}