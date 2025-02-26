package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusDTO.CreateUserStatusDTO createUserStatusDTO) {
        // User 존재 여부 확인
        if (!userRepository.existsById(createUserStatusDTO.getUserId())) {
            throw new RuntimeException("User not found");
        }

        // 이미 존재하는 UserStatus 확인
        UserStatus existingUserStatus = userStatusRepository.findByUserId(createUserStatusDTO.getUserId());
        if (existingUserStatus != null) {
            throw new RuntimeException("UserStatus already exists for this user");
        }

        // 새로운 UserStatus 생성
        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(createUserStatusDTO.getUserId());
        userStatus.setLastLoginAt(createUserStatusDTO.getLastLoginAt());
        userStatus.setCreatedAt(java.time.Instant.now());
        userStatus.setUpdatedAt(java.time.Instant.now());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStatus not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UUID id, UserStatusDTO.UpdateUserStatusDTO updateUserStatusDTO) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStatus not found"));

        // UserStatus 업데이트
        userStatus.setLastLoginAt(updateUserStatusDTO.getNewLastLoginAt());
        userStatus.setUpdatedAt(java.time.Instant.now());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusDTO.UpdateUserStatusDTO updateUserStatusDTO) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new RuntimeException("UserStatus not found for this user");
        }

        // UserStatus 업데이트
        userStatus.setLastLoginAt(updateUserStatusDTO.getNewLastLoginAt());
        userStatus.setUpdatedAt(java.time.Instant.now());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStatus not found"));

        // UserStatus 삭제
        userStatusRepository.delete(userStatus);
    }
}
