package com.sprint.mission.discodeit.service.featureBasicService;

import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    @Override
    public UserStatus create(UserStatusCreate userStatusCreate) {
        UUID userId = userStatusCreate.userId();
        if(!userRepository.existsById(userId)) {
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        }
        if(userStatusRepository.findByUserId(userId).isPresent()) {
            throw new NoSuchElementException(userId + "가 이미 존재합니다.");
        }
        Instant lastActiveAt = userStatusCreate.lastActiveAt();
        UserStatus userStatus = new UserStatus(userId, lastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException(userStatusId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public UserStatus upate(UUID userStatusId, UserStatusUpdate userStatusUpdate) {
       Instant newLastActiveAt = userStatusUpdate.lastActiveAt();
       UserStatus userStatus = userStatusRepository.findById(userStatusId)
               .orElseThrow(() -> new NoSuchElementException(userStatusId + "를 찾을 수 없습니다."));
       userStatus.update(newLastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate) {
        Instant newLastActiveAt = userStatusUpdate.lastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + "를 찾을 수 없습니다"));
        userStatus.update(newLastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if(!userStatusRepository.existsById(userStatusId)){
            throw new NoSuchElementException(userStatusId + "를 찾을 수 없습니다.");
        }
        userStatusRepository.deleteById(userStatusId);

    }
}
