package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

        private void checkDuplicateUsername(String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다: " + username);
        }
    }

    private void checkDuplicateEmail(String email) {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + email);
            }
        }
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        checkDuplicateUsername(request.username());
        checkDuplicateEmail(request.email());

        User user = new User(request.username(), request.email(), request.password());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserResponse.from(user, userStatus);
    }

    @Override
    public UserResponse createUserWithProfileImage(UserCreateRequest userRequest, BinaryContentCreateRequest imageRequest) {
        UserResponse user = createUser(userRequest);

        if (imageRequest != null) {
            BinaryContent profileImage = new BinaryContent(
                    imageRequest.fileNm(),
                    (long) imageRequest.content().length,
                    imageRequest.contentType(),
                    imageRequest.content()
            );
            binaryContentRepository.save(profileImage);
        }

        return user;
    }


    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }

        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            return null;
        }

        return UserResponse.from(user, userStatus);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();

        for (User user : users) {
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
            if (userStatus != null) {
                responses.add(UserResponse.from(user, userStatus));
            }
        }

        return responses;
    }

    @Override
    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }

        if (request.updateUsername() != null) {
            user.updateUsername(request.updateUsername());
        }
        if (request.updateEmail() != null) {
            user.updateEmail(request.updateEmail());
        }
        if (request.updatePassword() != null) {
            user.updatePassword(request.updatePassword());
        }

        userRepository.save(user);
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        return UserResponse.from(user, userStatus);
    }

    @Override
    public UserResponse updateUserWithProfileImage(UUID userId, UserUpdateRequest userRequest, BinaryContentCreateRequest imageRequest) {
        UserResponse updatedUser = updateUser(userId, userRequest);
        if (updatedUser == null) {
            return null;
        }

        if (imageRequest != null) {
            BinaryContent existingImage = binaryContentRepository.findByUserId(userId);
            if (existingImage != null) {
                binaryContentRepository.deleteById(existingImage.getId());
            }

            BinaryContent newProfileImage = new BinaryContent(
                    imageRequest.fileNm(),
                    (long) imageRequest.content().length,
                    imageRequest.contentType(),
                    imageRequest.content()
            );
            binaryContentRepository.save(newProfileImage);
        }

        return updatedUser;
    }



    @Override
    public boolean deleteUser(UUID userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return false;
        }

        userStatusRepository.deleteByUserId(userId);

        BinaryContent profileImage = binaryContentRepository.findByUserId(userId);
        if (profileImage != null) {
            binaryContentRepository.deleteById(profileImage.getId());
        }

        userRepository.deleteById(userId);
        return true;
    }
}