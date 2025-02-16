package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.FindUserResponse;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final MessageSource messageSource;

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;


    @Override
    public CreateUserResponse register(CreateUserRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        if (userRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.user.name.exist", new Object[]{name}, LocaleContextHolder.getLocale()));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.user.email.exist", new Object[]{email}, LocaleContextHolder.getLocale()));
        }

        User user = userMapper.toEntity(request);

        BinaryContent savedContent = request.getProfileImage();
        if (savedContent != null) {
            savedContent = binaryContentRepository.save(savedContent);
        }

        User createdUser = userRepository.save(user);
        userStatusRepository.save(new UserStatus(user.getId()));

        return userMapper.toCreateUserResponse(createdUser, savedContent);
    }

    @Override
    public FindUserResponse getUserDetails(UUID id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.user.id.notfound", null, LocaleContextHolder.getLocale()));
        }

        User user = optUser.get();

        BinaryContent userProfile = binaryContentRepository.findByUserIdAndType(user.getId(), BinaryContentType.PROFILE)
                .orElse(null);

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElse(null);

        return userMapper.toFindUserResponse(user, userProfile, userStatus);
    }

    //name, email, image, status
    @Override
    public List<FindUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<FindUserResponse> responses = new ArrayList<>();

        for (User user : users) {
            BinaryContent userProfile = binaryContentRepository.findByUserIdAndType(user.getId(), BinaryContentType.PROFILE)
                    .orElse(null);
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);

            responses.add(userMapper.toFindUserResponse(user, userProfile, userStatus));
        }

        return responses;
    }

    @Override
    public void updateUserProfile(UUID id, UpdateUserRequest request) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();

            if (request.getProfileImage() != null) {
                binaryContentRepository.save(request.getProfileImage());
                user.update(request.getName(), request.getEmail(), request.getPassword(), request.getProfileImage().getId());
            } else {
                user.update(request.getName(), request.getEmail(), request.getPassword(), null);
            }
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException(messageSource.getMessage("user.id.notfound", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void deleteUser(UUID id) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            User user = optUser.get();
            userStatusRepository.delete(id);
            binaryContentRepository.delete(user.getProfileId());

            userRepository.delete(id);
        }
    }
}
