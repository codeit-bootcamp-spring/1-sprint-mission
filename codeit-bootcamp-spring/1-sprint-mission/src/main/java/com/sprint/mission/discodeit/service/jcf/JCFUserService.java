package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_FOUND;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.user.UserConverter;
import com.sprint.mission.discodeit.service.user.UserService;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final UserRepository data;
    private final UserConverter converter;

    public JCFUserService(UserRepository userRepository, UserConverter converter) {
        this.data = userRepository;
        this.converter = converter;
    }

    @Override
    public UserInfoResponse register(RegisterUserRequest registerUserRequest) {
        var entity = converter.toEntity(registerUserRequest);
        var savedEntity = data.save(entity);
        return converter.toDto(savedEntity);
    }

    @Override
    public UserInfoResponse findUserByUsernameOrThrow(FindUserRequest findUserRequest) {
        var foundUser = data.findByUsername(findUserRequest.username())
                .filter(User::isNotUnregistered)
                .map(converter::toDto)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        return foundUser;
    }

    @Override
    public UserInfoResponse modifyUserInfo(ModifyUserInfoRequest request) {
        var foundUser = findByIdOrThrow(request.id());

        foundUser.changeUserName(request.changeUsername());
        data.save(foundUser);

        var response = converter.toDto(foundUser);
        return response;
    }

    @Override
    public void UnRegisterUser(UnregisterUserRequest request) {
        var foundUser = findByIdOrThrow(request.id());

        foundUser.unregister();

        data.save(foundUser);
    }

    private User findByIdOrThrow(UUID id) {
        var entity = data.findById(id)
                .filter(User::isNotUnregistered)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        return entity;
    }

    public static JCFUserService getInstance(UserRepository userRepository) {
        return new JCFUserService(userRepository, UserConverter.getInstance());
    }
}
