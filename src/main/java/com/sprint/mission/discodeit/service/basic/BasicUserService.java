package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    @Override
    public UserResponse createUser(UserRequest request, MultipartFile userProfileImage) {
        if (userValidator.isValidName(request.name()) && userValidator.isValidEmail(request.email()) && userValidator.isValidPassword(request.password())) {

            User newUser = User.createUser(request.name(), request.email(), request.password());
            userRepository.save(newUser);

            UserStatus newUserStatus = userStatusService.create(newUser.getId());

            UUID newBinaryContentId = null;
            if (userProfileImage != null) {
                BinaryContentResponse newBinaryContent = binaryContentService.createUserProfileFile(userProfileImage, newUser.getId());
                newBinaryContentId = newBinaryContent.id();
            }

            log.info("Create User: {}", newUser);
            return UserResponse.entityToDto(newUser, newUserStatus.getStatus(), newBinaryContentId);
        }
        return null;
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::entityToUserResponse
                )
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = findByIdOrThrow(id);
        return entityToUserResponse(user);
    }

    @Override
    public UserResponse update(UUID id, UserRequest request, MultipartFile userProfileImage) {
        User user = findByIdOrThrow(id);

        if (userValidator.isValidName(request.name()) && userValidator.isValidEmail(request.email()) && userValidator.isValidPassword(request.password())) {
            user.update(request.name(), request.email(), request.password());
            userRepository.save(user);

            if (userProfileImage != null) {
                binaryContentService.updateUserProfileFile(userProfileImage, id);
            }
        }
        log.info("Update User :{}", user);
        return entityToUserResponse(user);
    }

    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        binaryContentService.deleteByUserId(id);
        userStatusService.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public User findByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND, "id : " + id));
    }

    private UserResponse entityToUserResponse(User user) {
        UserStatus.Status userStatus =  userStatusService.findByUserId(user.getId()).getStatus();
        UUID binaryContentId = null;
        BinaryContentResponse binaryContentResponse = binaryContentService.findByUserId(user.getId());
        if (binaryContentResponse != null) {
            binaryContentId = binaryContentResponse.id();
        }
        return UserResponse.entityToDto(user, userStatus, binaryContentId);
    }

}
