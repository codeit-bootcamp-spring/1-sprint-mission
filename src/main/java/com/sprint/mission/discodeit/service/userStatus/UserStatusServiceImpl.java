package com.sprint.mission.discodeit.service.userStatus;


import com.sprint.mission.discodeit.dto.userStatusService.UserStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.userStatusService.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Setter
@Qualifier("BasicUserStatusService")
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequestDTO request) {
        if(!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User with id not found");
        }

        if(userStatusRepository.existsByUserId(request.userId())) {
            throw new NoSuchElementException("User with id not found");
        }

        UserStatus userStatus = request.from();

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDTO request) {
        UserStatus userStatus = userStatusRepository.findById(request.userStatusId())
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id not found"));

        if(request.lastActive() != null) {
            userStatus.updateLastActive(request.lastActive());
        }

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id not found"));

        userStatus.updateLastActive(Instant.now());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {

        if(!userRepository.existsById(id)) {
            throw new NoSuchElementException("User with id not found");
        }
        userStatusRepository.deleteByuserId(id);
    }


}
