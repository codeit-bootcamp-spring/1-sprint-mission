package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;
    private final MessageSource messageSource;

    public UserStatusResponse create(CreateUserStatusRequest request) {
        if (!userRepository.findById(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.user.id.notfound", new Object[]{request.getUserId()}, LocaleContextHolder.getLocale()));
        }

        if (userStatusRepository.findAll().stream()
                .anyMatch(status -> status.getUserId().equals(request.getUserId()))) {
            throw new IllegalStateException(messageSource.getMessage("error.user.id.exist", new Object[]{request.getUserId()}, LocaleContextHolder.getLocale()));
        }

        UserStatus userStatus = userStatusMapper.toEntity(request);
        UserStatus savedStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toUserStatusResponse(savedStatus);
    }

    public UserStatusResponse find(UUID id) {
        return userStatusRepository.findById(id)
                .map(userStatusMapper::toUserStatusResponse)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("error.userstatus.id.notfound", new Object[]{id}, LocaleContextHolder.getLocale())));
    }

    public List<UserStatusResponse> findAll() {
        List<UserStatus> statuses = userStatusRepository.findAll();
        return userStatusMapper.toUserStatusResponseList(statuses);
    }

    public UserStatusResponse update(UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("error.userstatus.id.notfound", new Object[]{request.getUserId()}, LocaleContextHolder.getLocale())));

        userStatus.update();
        UserStatus updatedStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toUserStatusResponse(updatedStatus);
    }

    public UserStatusResponse updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found for user: " + userId));

        userStatus.update();
        UserStatus updatedStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toUserStatusResponse(updatedStatus);
    }

    public void delete(UUID id) {
        if (!userStatusRepository.delete(id)) {
            throw new IllegalArgumentException(messageSource.getMessage("error.userstatus.id.notfound", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }
}