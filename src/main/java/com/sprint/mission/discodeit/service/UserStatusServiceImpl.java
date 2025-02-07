package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    @Autowired
    private final UserStatusRepository userStatusRepository;
    @Autowired
    private final UserRepository userRepository;


    @Override
    public void create(UserStatusRequest request) {
        if (!userRepository.existsById(request.getUserId())){
            throw new NoSuchElementException("User not found");
        }
        Optional<UserStatus> existingStatus=userStatusRepository.findByUserId(request.getUserId());
        if(existingStatus.isPresent()){
            throw new IllegalArgumentException("UserStatus already exists");
        }
        UserStatus userStatus=new UserStatus(request.getUserId(),request.getStatus());
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User status not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public void update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
        userStatus.setUserStatus(request.getStatus());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UUID userId, String status) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found for user"));
        userStatus.setUserId(userId);
        userStatus.updateLastSeenAt();
        userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if(!userStatusRepository.existsById(id)){
            throw new NoSuchElementException("UserStatus not found");
        }
        userStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusRepository.deleteByUserId(userId);
    }
}
