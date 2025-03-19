package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusResponseDto create(UserStatus userStatus) {
        return userStatusMapper.toResponseDto(userStatusRepository.save(userStatus));
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        return userStatusRepository.findAll().stream().map(userStatusMapper::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserStatusResponseDto update(UUID userId, UserStatusRequestDto userStatusRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        UserStatus userStatus = userStatusRepository.findByUser(user);
        userStatus.updateLastActiveAt(userStatusRequestDto.newLastActiveAt());

        return userStatusMapper.toResponseDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusResponseDto delete(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        userStatusRepository.delete(userStatus);
        return userStatusMapper.toResponseDto(userStatus);
    }
}
