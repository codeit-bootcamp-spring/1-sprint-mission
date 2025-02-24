package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final UserValidator userValidator;
    private final UserStatusValidator userStatusValidator;

    private final MultipartFileConverter multipartFileConverter;

    @Override
    public User createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile) {

        // name 중복 여부
        userValidator.validateDuplicateByName(createUserRequest.name());
        // email 중복 여부
        userValidator.validateDuplicateUserByEmail(createUserRequest.email());

        User user = userMapper.toEntity(createUserRequest);

        // 이미지는 선택적으로 등록
        updateProfileImage(user, profileImageFile);

        // UserStatus 생성(추후 service layer로 교체)
        userStatusRepository.saveUserStatus(UserStatus.of(user));

        return userRepository.saveUser(user);
    }

    @Override
    public FindUserResponse findUserByIdOrThrow(UUID userId) {
        User foundUser = userValidator.validateUserExistsByUserId(userId);
        UserStatus userStatus = userStatusValidator.validateUserStatusExistsByUser(foundUser);

        byte[] profileImage = foundUser.getProfileImage().getContent();

        return userMapper.toFindUserResponse(foundUser, profileImage, userStatus.isOnline());
    }

    @Override
    public List<FindUserResponse> findAllUsers() {
        return userRepository.findAllUsers().stream()
                .map(user -> findUserByIdOrThrow(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(UUID userId, UpdateUserRequest updateUserRequest, MultipartFile profileImageFile) {
        User foundUser = userValidator.validateUserExistsByUserId(userId);

        foundUser.updateUserInfo(updateUserRequest.name(), updateUserRequest.nickname(),
                updateUserRequest.email(), updateUserRequest.password());

        updateProfileImage(foundUser, profileImageFile);

        return foundUser;
    }

    @Override
    public void deleteUser(UUID userId) {
        // 유저 존재 여부 확인
        User foundUser = userValidator.validateUserExistsByUserId(userId);

        // UserStatus 삭제
        UserStatus userStatus = userStatusValidator.validateUserStatusExistsByUser(foundUser);
        // service?
        userStatusRepository.removeUserStatus(userStatus.getId());

        // BinaryContent 삭제
        // BinaryContent 존재 여부 확인 -> validator or Service
        // binaryContentService deleteBinaryContent로 검증 및 삭제까지 가능
        binaryContentRepository.removeBinaryContent(foundUser.getProfileImage().getId());

        // 참여한 채널에서도 유저 삭제

        // 유저 삭제
        userRepository.removeUser(userId);
    }

    private void updateProfileImage(User user, MultipartFile profileImageFile) {
        if (profileImageFile != null) {
            BinaryContent binaryContent = binaryContentRepository.saveBinaryContent(
                    BinaryContent.of(multipartFileConverter.toByteArray(profileImageFile)));

            user.updateProfileImage(binaryContent);
        }
    }
}
