package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentDeleteService;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentRegisterService;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDeleteResponse;
import com.sprint.mission.discodeit.service.dto.user.UserRegisterRequest;
import com.sprint.mission.discodeit.service.dto.user.UserRegisterResponse;
import com.sprint.mission.discodeit.service.dto.user.UserSearchResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateResponse;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.readstatus.ReadStatusDeleteService;
import com.sprint.mission.discodeit.service.readstatus.ReadStatusRegisterService;
import com.sprint.mission.discodeit.service.readstatus.ReadStatusUpdateService;
import com.sprint.mission.discodeit.service.userstatus.UserStatusDeleteService;
import com.sprint.mission.discodeit.service.userstatus.UserStatusRegisterService;
import com.sprint.mission.discodeit.service.userstatus.UserStatusUpdateService;
import com.sprint.mission.discodeit.validation.UserValidator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FileUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ReadStatusRegisterService readStatusRegisterService;
    private final UserStatusRegisterService userStatusRegisterService;
    private final BinaryContentRegisterService binaryContentRegisterService;
    private final UserStatusUpdateService userStatusUpdateService;
    private final ReadStatusUpdateService readStatusUpdateService;
    private final UserStatusDeleteService userStatusDeleteService;
    private final ReadStatusDeleteService readStatusDeleteService;
    private final BinaryContentDeleteService binaryContentDeleteService;

    public FileUserService(UserRepository userRepository,
        ReadStatusRegisterService readStatusRegisterService,
        UserStatusRegisterService userStatusRegisterService,
        BinaryContentRegisterService binaryContentRegisterService,
        UserStatusUpdateService userStatusUpdateService,
        ReadStatusUpdateService readStatusUpdateService,
        UserStatusDeleteService userStatusDeleteService,
        ReadStatusDeleteService readStatusDeleteService,
        BinaryContentDeleteService binaryContentDeleteService) {
        this.userRepository = userRepository;
        this.userValidator = UserValidator.getInstance();
        this.readStatusRegisterService = readStatusRegisterService;
        this.userStatusRegisterService = userStatusRegisterService;
        this.binaryContentRegisterService = binaryContentRegisterService;
        this.userStatusUpdateService = userStatusUpdateService;
        this.readStatusUpdateService = readStatusUpdateService;
        this.userStatusDeleteService = userStatusDeleteService;
        this.readStatusDeleteService = readStatusDeleteService;
        this.binaryContentDeleteService = binaryContentDeleteService;
    }

    /**
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from
     * {@code userInfoToCreate}
     */
    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request)
        throws InvalidFormatException {

        UUID userId = UUID.randomUUID();

        UserStatus userStatus = userStatusRegisterService.registerUserStatus(userId);
        ReadStatus readStatus = readStatusRegisterService.registerReadStatus(userId);
        BinaryContent image = binaryContentRegisterService.registerBinaryContent(userId);

        User userToCreate = User.builder(request.name(), request.email())
            .phoneNumber(request.phoneNumber())
            .userStatus(userStatus)
            .readStatus(readStatus)
            .image(image)
            .build();

        validateFormat(userToCreate);

        User createdUser = userRepository.createUser(userToCreate);

        return UserRegisterResponse.from(createdUser);
    }

    @Override
    public UserSearchResponse searchUserById(UUID key) {

        User userById = userRepository.findUserById(key);

        return UserSearchResponse.from(userById);
    }

    @Override
    public List<UserSearchResponse> searchAllUser() {
        return userRepository.findAllUser().stream()
            .map(UserSearchResponse::from)
            .toList();
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from
     * {@code userInfoToUpdate}
     */
    @Override
    public UserUpdateResponse updateUserById(UserUpdateRequest request)
        throws InvalidFormatException {

        UUID userId = request.id();

        UserStatus userStatus = userStatusUpdateService.update(
            UserStatusUpdateRequest.from(request.userStatus()));
        ReadStatus readStatus = readStatusUpdateService.update(
            ReadStatusUpdateRequest.from(request.readStatus()));
        BinaryContent image = binaryContentRegisterService.registerBinaryContent(userId);

        User userInfoToUpdate = User.builder(request.name(), request.email())
            .phoneNumber(request.phoneNumber())
            .userStatus(userStatus)
            .readStatus(readStatus)
            .image(image)
            .build();

        validateFormat(userInfoToUpdate);

        User updatedUser = userRepository.updateUserById(userId, userInfoToUpdate);

        return UserUpdateResponse.from(updatedUser);
    }

    @Override
    public UserDeleteResponse deleteUserById(UUID userId) {

        userStatusDeleteService.deleteById(userId);
        readStatusDeleteService.deleteById(userId);
        binaryContentDeleteService.deleteById(userId);

        User deleteUserById = userRepository.deleteUserById(userId);

        return UserDeleteResponse.from(deleteUserById);
    }

    private void validateFormat(User userInfoToCreate) throws InvalidFormatException {
        userValidator.validateIdFormat(userInfoToCreate);
        userValidator.validateCreateAtFormat(userInfoToCreate);
        userValidator.validateUpdateAtFormat(userInfoToCreate);
        userValidator.validateNameFormat(userInfoToCreate);
        userValidator.validateEmailFormat(userInfoToCreate);
    }
}
