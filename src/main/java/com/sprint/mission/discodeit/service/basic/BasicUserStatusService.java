package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus findById(String userStatusId) {
        return userStatusRepository.findById(userStatusId);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus create(CreateUserStatusDto createUserStatusDto) throws CustomException {
        if (userRepository.findById(createUserStatusDto.userId()) == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (userStatusRepository.findById(createUserStatusDto.userId()) != null) {
            throw new IllegalArgumentException("userStatus already exists");
        }
        UserStatus userStatus = new UserStatus(createUserStatusDto.userId());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(String userStatusId, UpdateUserStatusDto updateUserStatusDto) {
        
        if (userRepository.findById(updateUserStatusDto.userId()) == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        UserStatus userStatus = userStatusRepository.findById(userStatusId);
        if (userStatus == null) {
            throw new IllegalArgumentException("userStatus not found");
        }

        if (userStatus.isUpdated(updateUserStatusDto)) {
            return userStatusRepository.save(userStatus);
        }

        return userStatus;
    }

    @Override
    public boolean delete(String userStatusId) {
        return userStatusRepository.delete(userStatusId);
    }
}
