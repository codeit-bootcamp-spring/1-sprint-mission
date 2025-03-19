package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UUID create(UUID userId) {

        User user = userService.find(userId);  // 유저 생성 시 userStatus도 생성됨
        UserStatus userStatus = user.getStatus();

        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public UserStatusDto find(UUID id) {

        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저 상태입니다."));

        return UserStatusMapper.INSTANCE.toDto(userStatus);
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public void update(UUID id) {

        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저 상태입니다."));
        userStatus.updateLastAccessTime();

        userStatusRepository.save(userStatus);
    }

    @Override
    public UserDto updateByUserId(UUID userId) {

        User user = userService.find(userId);

        UserStatus userStatus = user.getStatus();
        userStatus.updateLastAccessTime();

        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
