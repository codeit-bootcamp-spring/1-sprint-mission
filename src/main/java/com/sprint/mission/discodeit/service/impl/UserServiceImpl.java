package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Override
    public User create(CreateUserDTO createUserDTO) {
        // username과 email 중복 확인
        if (userRepository.existsByUsername(createUserDTO.getUsername()) ||
                userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new RuntimeException("Username or Email already exists.");
        }

        // User 객체 생성
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());

        // 프로필 이미지가 제공되면 설정
        if (createUserDTO.getProfileImage() != null) {
            BinaryContent binaryContent = new BinaryContent();
            binaryContent.setUserId(user.getId());
            binaryContent.setContent(createUserDTO.getProfileImage());  // 이미지 데이터
            binaryContentRepository.save(binaryContent);
        }

        // UserStatus 생성
        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(user.getId());
        userStatus.setLastActiveAt(LocalDateTime.now());
        userStatus.setIsActive(true);  // 기본값으로 true 설정

        userRepository.save(user);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserResponseDTO find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // UserStatus 조회
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("UserStatus not found"));

        // UserResponseDTO로 반환
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                userStatus.getLastActiveAt(),
                userStatus.isActive()
        );
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(user -> {
            // UserStatus 조회
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow();

            return new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    userStatus.getLastActiveAt(),
                    userStatus.isActive()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 새로운 프로필 이미지가 있다면 기존 이미지 삭제 후 새로운 이미지 저장
        if (updateUserDTO.getNewProfileImage() != null) {
            binaryContentRepository.deleteByUserId(userId);  // 기존 이미지 삭제
            BinaryContent binaryContent = new BinaryContent();
            binaryContent.setUserId(userId);
            binaryContent.setContent(updateUserDTO.getNewProfileImage());  // 새로운 이미지 데이터
            binaryContentRepository.save(binaryContent);
        }

        // 사용자 정보 업데이트
        if (updateUserDTO.getUsername() != null) {
            user.setUsername(updateUserDTO.getUsername());
        }
        if (updateUserDTO.getEmail() != null) {
            user.setEmail(updateUserDTO.getEmail());
        }
        if (updateUserDTO.getPassword() != null) {
            user.setPassword(updateUserDTO.getPassword());
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        // UserStatus와 BinaryContent 삭제
        userStatusRepository.deleteByUserId(userId);
        binaryContentRepository.deleteByUserId(userId);

        // 사용자 삭제
        userRepository.deleteById(userId);
    }
}
