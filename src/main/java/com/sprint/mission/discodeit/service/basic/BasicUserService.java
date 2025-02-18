package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(CreateUserDTO createUserDTO) {
        User user = new User(createUserDTO.getUsername(), createUserDTO.getPassword(), createUserDTO.getEmail());
        return userRepository.save(user);
    }

    @Override
    public UserResponseDTO find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        return new UserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(updateUserDTO.getUsername(), updateUserDTO.getPassword(), updateUserDTO.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }
}
