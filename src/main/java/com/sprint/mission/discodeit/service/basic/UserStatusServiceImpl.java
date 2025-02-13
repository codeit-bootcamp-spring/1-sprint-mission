package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus createUserStatus(UserStatusCreateRequest userStatusCreateRequest) {
        UserStatus userStatus = new UserStatus(userStatusCreateRequest.lastConnectTime());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findUserStatusById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseGet(() -> {
                    System.out.println("No " + userStatusId + " UserStatus");
                    return null;
                });
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest) {
        // UserStatus의 id기준으로 바꾸는 것으로 이해
        UserStatus userStatus =
                userStatusRepository.findById(userStatusUpdateRequest.UserStatusId())
                        .orElseGet(()-> {
                            System.out.println("No " + userStatusUpdateRequest.UserStatusId() + " UserStatus");
                            return null;
                        });

        userStatus.refreshLastConnectTime(userStatusUpdateRequest.lastConnectTime());
        userStatus.refreshNow();
        userStatus.refreshUpdateAt();
        return userStatus;
    }

    @Override
    public UserStatus updateUserStatusByUserId(UUID userId, Instant lastConnectTime) {
        UserStatus userStatus =
                userStatusRepository.findByUserId(userId)
                .orElseGet(()->{
                    System.out.println("No " + userId + " UserStatus");
                    return null;
                });

        userStatus.refreshLastConnectTime(lastConnectTime);
        userStatus.refreshNow();
        userStatus.refreshUpdateAt();
        return userStatus;
    }

    @Override
    public void deleteUserStatusById(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);
    }
}
