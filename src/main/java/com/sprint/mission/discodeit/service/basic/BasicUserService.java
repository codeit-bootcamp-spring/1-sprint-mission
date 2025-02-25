package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.aspect.UpdateUserStatus;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.type.OnlineStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusService userStatusService;

    @Override
    public UserDTO create(CreateUserRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(x -> {
                    throw new ServiceException(ErrorCode.DUPLICATE_EMAIL);});

        userRepository.findByName(request.name()).ifPresent(x -> {
            throw new ServiceException(ErrorCode.DUPLICATE_NAME);}); // 유저의 이름이 같으면 안된다.

        if (request.profileId() != null) {
            binaryContentRepository.findById(request.profileId())
                    .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_PROFILE));
        }

        User createdUser = new User(request.name(), request.email(), request.password(), request.profileId(), null);
        userRepository.save(createdUser); // User 레포지토리에 저장하기

        Instant lastActiveAt = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getId(), lastActiveAt);// UserStatus 생성
        createdUser.setUserStatusId(userStatus.getId());

        userStatusRepository.save(userStatus);

        return UserDTO.fromDomain(createdUser);
    }

    @Override
    public UserDTO find(UUID userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
        UserStatus status = userStatusRepository.findByUserId(findUser.getId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        return UserDTO.fromDomain(findUser);
    }

    @Override
    public List<UserDTO> getOnlineUsers() {
        List<UUID> onlineUserIds = userStatusRepository.findByIsOnlineTrue().stream()
                .map(UserStatus::getUserId)
                .toList();

        return userRepository.findAllById(onlineUserIds).stream()
                .map(UserDTO::fromDomain)
                .toList();
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @UpdateUserStatus
    @Override
    public User updatePassword(UUID userId, UpdatePasswordRequest request) {
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
        updateUser.updatePassword(request.oldPassword(), request.newPassword()); // 비밀번호를 수정합니다.

        userRepository.save(updateUser);
        return updateUser;
    }

    @UpdateUserStatus
    @Override
    public User updateProfile(UUID userId, UpdateProfileRequest request) {
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
        BinaryContent profile = binaryContentRepository.findById(request.profileId()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_PROFILE));

        updateUser.updateProfile(request.profileId(), request.newProfileId());
        userRepository.save(updateUser);
        return updateUser;
    }


    @Override
    public UserDTO delete(UUID id) {
        User deleteUser = userRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));

        if (deleteUser.getProfileImageId() != null) { // 만약 프로필이 등록되어 있다면 해당 프로필도 같이 삭제
            binaryContentRepository.deleteById(deleteUser.getProfileImageId());
        }
        userStatusRepository.deleteByUserId(deleteUser.getId()); // UserStatus 도 같이 삭제

        userRepository.delete(id);

        return UserDTO.fromDomain(deleteUser);
    }
}
