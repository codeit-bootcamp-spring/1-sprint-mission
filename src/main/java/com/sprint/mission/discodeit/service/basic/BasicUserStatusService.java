package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusValidator userStatusValidator;

    @Override
    public UUID create(UserStatusCreateDTO dto) {
        userStatusValidator.validateUserStatus(dto.getUserid());

        UserStatus userStatus = new UserStatus(dto.getUserid());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        UserStatus findUserStatus = userStatusRepository.find(id);
        Optional.ofNullable(findUserStatus)
                .orElseThrow(() -> new NoSuchElementException("해당 UserStatus 가 없습니다."));

        //접속시간 업데이트하고 > 사용자가 접속했다는 것을 어떻게 아냐?
        //UserService의 updateUserOnline로 상태 업데이트
        findUserStatus.isOnline();
        return findUserStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream()
                .peek(UserStatus::isOnline)
                .toList();
    }

    @Override
    public UserStatus update(UUID userId, UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus findUserStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userid:" + userId + "의 UserStatus 가 없습니다."));
        findUserStatus.updateLastActiveAt(userStatusUpdateDTO.getTime());
        userStatusRepository.update(findUserStatus);
        return findUserStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId,  Instant time) {
        UserStatus finduserStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userid:" + userId + "의 UserStatus 가 없습니다."));

        finduserStatus.updateLastActiveAt(time);
        userStatusRepository.update(finduserStatus);
        return finduserStatus;
    }

    @Override
    public UUID delete(UUID id) {
        return userStatusRepository.delete(id);
    }

}
