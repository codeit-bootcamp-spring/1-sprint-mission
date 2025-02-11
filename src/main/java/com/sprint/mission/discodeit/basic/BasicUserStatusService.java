package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusDTO create(UserStatusDTO userStatusDTO) {
        UserStatus userStatus = new UserStatus(userStatusDTO.getUserId(), Instant.now());
        userStatusRepository.save(userStatus);
        return new UserStatusDTO(userStatus.getUserId(), userStatus.getLastSeen());
    }

    @Override
    public UserStatusDTO find(String userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
        return new UserStatusDTO(userStatus.getUserId(), userStatus.getLastSeen());
    }

    @Override
    public void delete(String userId) {
        userStatusRepository.deleteByUserId(userId);
    }
}