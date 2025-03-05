package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.EntryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public Long create(UserStatusDTO.request userStatusDTO) {
        try {
            return userStatusRepository.save(new UserStatus(userStatusDTO.userId(), userStatusDTO.accessedAt()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserStatusDTO.response find(Long id) {
        UserStatus userStatus = userStatusRepository.load(id);
        if (userStatus == null) throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        return UserStatusDTO.response.from(EntryUtils.of(id, userStatus));
    }

    @Override
    public UserStatusDTO.response find(UUID uuid) {
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.load(uuid);
        if (userStatus == null) throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        return UserStatusDTO.response.from(userStatus);
    }

    @Override
    public UserStatusDTO.response findByUserId(UUID userid) {
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(userid);
        return UserStatusDTO.response.from(userStatus);
    }

    @Override
    public List<UserStatusDTO.response> findAll() {
        Map<Long, UserStatus> userStatuses = userStatusRepository.loadAll();
        return userStatuses.entrySet().stream()
                .map(UserStatusDTO.response::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommonDTO.idResponse update(Long statusId, UUID userId, Instant accessAt) {
        boolean isUpdated = false;
        UserStatusDTO.update updateDTO = UserStatusDTO.update.of(statusId, userId, accessAt);
        try {
            UserStatus userStatus = userStatusRepository.load(updateDTO.id());
            if (userStatus == null) {
                throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
            }

            // 값 변경 로직 추가
            if (updateDTO.accessedAt() != null) {
                userStatus.updateAccessedAt();
                isUpdated = true;
            }

            userStatusRepository.update(updateDTO.id(), userStatus);
            return CommonDTO.idResponse.from(updateDTO.id(), userStatus.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonDTO.idResponse updateByUserId(UUID userId, UserStatusDTO.update updateDTO) {
        boolean isUpdated = false;
        try {
            Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(userId);
            if (userStatus == null) {
                throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
            }

            // 값 변경 로직 추가
            if (updateDTO.accessedAt() != null) {
                userStatus.getValue().updateAccessedAt();
                isUpdated = true;
            }

            // TODO: 삼항연산자를 넣는게 나을까?
            userStatusRepository.update(userStatus.getKey(), userStatus.getValue());
            return CommonDTO.idResponse.from(userStatus.getKey(), userStatus.getValue().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long delete(Long id) {
        return userStatusRepository.delete(id);
    }

    @Override
    public Long delete(UUID uuid) {
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.load(uuid);
        return userStatusRepository.delete(userStatus.getKey());
    }
}
