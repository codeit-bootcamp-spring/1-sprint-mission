package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            return userStatusRepository.save(new UserStatus(userStatusDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserStatusDTO.response find(Long id) {
        UserStatus userStatus = userStatusRepository.load(id);
        if (userStatus == null) throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        return UserStatusDTO.response.builder()
                .id(id)
                .uuid(userStatus.getId())
                .userId(userStatus.getUserId())
                .accessedAt(userStatus.getAccessedAt())
                .build();
    }

    @Override
    public UserStatusDTO.response find(UUID uuid) {
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.load(uuid);
        if (userStatus == null) throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        return UserStatusDTO.response.builder()
                .id(userStatus.getKey())
                .uuid(userStatus.getValue().getId())
                .userId(userStatus.getValue().getUserId())
                .accessedAt(userStatus.getValue().getAccessedAt())
                .build();
    }

    @Override
    public List<UserStatusDTO.response> findAll() {
        Map<Long, UserStatus> userStatuses = userStatusRepository.loadAll();
        return userStatuses.entrySet().stream()
                .map(entry -> UserStatusDTO.response.builder()
                        .id(entry.getKey())
                        .uuid(entry.getValue().getId())
                        .userId(entry.getValue().getUserId())
                        .accessedAt(entry.getValue().getAccessedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(UserStatusDTO.update updateDTO) {
        boolean isUpdated = false;
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
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateByUserId(UUID userId, UserStatusDTO.update updateDTO) {
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

            userStatusRepository.update(userStatus.getKey(), userStatus.getValue());
            return isUpdated;
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
