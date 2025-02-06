package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.domain.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService implements UserStatusRepository {
    //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus save(UserStatusDto userStatusDto) throws IllegalAccessException {
        if (userRepository.findUserById(userStatusDto.userId()) == null) {
            throw new IllegalStateException("유저가 존재하지 않습니다.");
        }
        if( userStatusRepository.findById(userStatusDto.userId()) != null ){
            throw new IllegalStateException("유저에 대한 상태 값이 이미 존재합니다.");
        }
        return userStatusRepository.save(userStatusDto);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }

    @Override
    public void update(UserStatusDto userStatusDto) { //활동 시간 업데이트 -> 상태 업데이트
        userStatusRepository.update(userStatusDto);
    }

    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        userStatusRepository.update(new UserStatusDto(userStatus));

    }
}
