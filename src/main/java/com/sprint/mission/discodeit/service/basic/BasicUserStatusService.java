package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (userStatusRepository.existsByUser(user)) {
            throw new IllegalArgumentException("UserStatus already exists for this user.");
        }

        UserStatus userStatus = new UserStatus(user, request.getLastSeenAt());
        userStatusRepository.save(userStatus);

        return new UserStatusResponse(userStatus);
    }

    @Override
    public UserStatusResponse findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        UserStatus userStatus = userStatusRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found for this user"));

        return new UserStatusResponse(userStatus);
    }


    @Override
    public List<UserStatusResponse> findAll(List<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        List<UserStatus> userStatuses = userStatusRepository.findAllByUserIn(users);
        return userStatuses.stream().map(UserStatusResponse::new).collect(Collectors.toList());
    }



    @Override
    public UserStatusResponse updateByUserId(UserStatusUpdateByUserIdRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        UserStatus userStatus = userStatusRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found for this user"));

        userStatus.updateLastSeen(request.getLastSeenAt());
        userStatusRepository.save(userStatus);

        return new UserStatusResponse(userStatus);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!userStatusRepository.existsByUser(user)) {
            throw new NoSuchElementException("UserStatus not found for this user");
        }

        userStatusRepository.deleteByUser(user);
    }

}
