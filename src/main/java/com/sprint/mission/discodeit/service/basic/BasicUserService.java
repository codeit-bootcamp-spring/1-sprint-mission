package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
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

            if (userProfileImage != null) {
                binaryContentService.createUserProfileFile(userProfileImage, newUser.getId());
            }

            log.info("Create User: {}", newUser);
            return UserResponse.entityToDto(newUser, newUserStatus);
        }
        return null;
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.entityToDto(user, userStatusService.findByUserId(user.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = findByIdOrThrow(id);
        return UserResponse.entityToDto(user, userStatusService.findByUserId(user.getId()));
    }

    @Override
    public UserResponse update(UUID id, UserRequest request, MultipartFile userProfileImage) {
        User user = findByIdOrThrow(id);
        String newName = null;
        String newEmail = null;
        String newPassword = null;

        if (request.name() != null && userValidator.isValidName(request.name())) {
            newName = request.name();
        }
        if (request.email() != null && userValidator.isValidEmail(request.email())) {
            newEmail = request.email();
        }
        if (request.password() != null && userValidator.isValidPassword(request.password())) {
            newPassword = request.password();
        }
        user.update(newName, newEmail, newPassword);
        userRepository.save(user);

        if (userProfileImage != null) {
            binaryContentService.updateUserProfileFile(userProfileImage, id);
        }
        log.info("Update User :{}", user);
        return UserResponse.entityToDto(user, userStatusService.findByUserId(user.getId()));
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
                .orElseThrow(() -> new NoSuchElementException("User does not exist"));
    }
}
