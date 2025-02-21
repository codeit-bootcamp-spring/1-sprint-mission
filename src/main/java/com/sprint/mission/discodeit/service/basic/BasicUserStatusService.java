package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusDto.FindUserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UUID create(UUID userId) {

        userService.userIsExist(userId);

        User user = userRepository.load().get(userId);  // 유저 생성 시 userStatus도 생성됨
        UserStatus userStatus = user.getUserStatus();

        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public FindUserStatusResponseDto find(UUID id) {

        UserStatus userStatus = userStatusRepository.load().get(id);

        return new FindUserStatusResponseDto(userStatus);
    }

    @Override
    public List<FindUserStatusResponseDto> findAll() {
        return userStatusRepository.load().values().stream()
                .map(FindUserStatusResponseDto::new)
                .toList();
    }

    @Override
    public void update(UUID id) {
        UserStatus userStatus = userStatusRepository.load().get(id);
        userStatus.updateLastAccessTime();

        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UUID userId) {
        User user = userRepository.load().get(userId);

        UserStatus userStatus = user.getUserStatus();
        userStatus.updateLastAccessTime();

        userStatusRepository.save(userStatus);
        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
