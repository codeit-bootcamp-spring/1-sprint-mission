package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    public User create(UsersDTO dto, byte[] imageBytes) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setOnline(true);

        if (imageBytes != null && imageBytes.length > 0) {
            user.setProfileImage(imageBytes);
        }

        return userRepository.save(user);
    }

    @Override
    public User update(String id, UsersDTO usersDTO, byte[] profileImage) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Objects.requireNonNull(usersDTO, "UserDTO cannot be null");

        userRepository.save(user);

        if (profileImage != null) {
            binaryContentRepository.deleteById(user.getId());
            BinaryContent newProfile = new BinaryContent(user.getId(), profileImage);
            binaryContentRepository.save(newProfile);
        }

        return user;
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
        binaryContentRepository.deleteById(id);
    }

    @Override
    public UserDTO find(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    private UsersDTO toDTO(User user) {
        UsersDTO dto = new UsersDTO();
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

    public List<UsersDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void updateOnlineStatus(String userId, boolean online) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setOnline(online);

        userRepository.save(user);
    }
}