package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserDTO userDTO, byte[] profileImage) {
        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        userRepository.save(user);

        if (profileImage != null) {
            BinaryContent profile = new BinaryContent(user.getId(), profileImage);
            binaryContentRepository.save(profile);
        }

        return user;
    }

    @Override
    public User update(String id, UserDTO userDTO, byte[] profileImage) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Objects.requireNonNull(userDTO, "UserDTO cannot be null");

        user.update(
                userDTO.getName() != null ? userDTO.getName() : user.getName(),
                userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail()
        );

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
        return new UserDTO(user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getName(), user.getEmail(), user.getPassword()))
                .collect(Collectors.toList());
    }
}