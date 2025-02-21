package com.example.mission.discodeit.service;

import com.example.mission.discodeit.entity.User;
import com.example.mission.discodeit.dto.UserDto;
import com.example.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .profileId(userDto.getProfileId()) // 프로필 사진 ID (없으면 null)
                .online(userDto.getOnline() != null ? userDto.getOnline() : false)
                .build();
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(UUID id, UserDto userDto) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setProfileId(userDto.getProfileId());
            user.setOnline(userDto.getOnline());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public void updateOnlineStatus(UUID id, boolean status) {
        userRepository.findById(id).ifPresent(user -> {
            user.setOnline(status);
            userRepository.save(user);
        });
    }

//    public void updateProfileId(UUID userId, UUID profileId) {
//        userRepository.findById(userId).ifPresent(user -> {
//            user.setProfileId(profileId);
//            userRepository.save(user);
//        });
//    }
}
