package com.sprint.mission.discodeit.study;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempUserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public User createUser(CreateUserRequest createUserRequest) {
        // name 중복 여부
        userValidator.validateDuplicateByName(createUserRequest.name());
        // email 중복 여부
        userValidator.validateDuplicateUserByEmail(createUserRequest.email());

        User user = User.of(createUserRequest.name(), createUserRequest.nickname(), createUserRequest.email(),
                createUserRequest.password());

        return userRepository.saveUser(user);
    }

    public User findUserByIdOrThrow(UUID userId) {
        return userValidator.validateUserExistsByUserId(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers().stream()
                .map(user -> findUserByIdOrThrow(user.getId()))
                .collect(Collectors.toList());
    }

//    public User updateUser(UpdateUserRequest updateUserRequest, MultipartFile profileImageFile) {
//        User foundUser = userValidator.validateUserExistsByUserId(updateUserRequest.userId());
//
//        foundUser.updateUserInfo(updateUserRequest.name(), updateUserRequest.nickname(),
//                updateUserRequest.email(), updateUserRequest.password());
//
//        return foundUser;
//    }

    public void deleteUser(UUID userId) {
        userRepository.removeUser(userId);
    }
}
