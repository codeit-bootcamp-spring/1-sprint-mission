package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.USER_NOT_FOUND;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.User;
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
    public UserInfoResponse findUserByUsername(FindUserRequest findUserRequest) {
        var entity = data.findByUsername(findUserRequest.username())
                .filter(user -> user.getStatus() != UNREGISTERED)
                .map(converter::toDto)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage()));
        return entity;
    }

    @Override
    public UserInfoResponse modifyUserInfo(ModifyUserInfoRequest request) {
        var entity = findById(request.id());

        entity.changeName(request.changeUsername());
        data.save(entity);

        var response = converter.toDto(entity);
        return response;
    }

    @Override
    public void UnRegisterUser(UnregisterUserRequest request) {
        var entity = findById(request.id());

        entity.unregister();

        data.save(entity);
    }


    private User findById(UUID id) {
        var entity = data.findById(id)
                .filter(user -> user.getStatus() != UNREGISTERED)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage()));
        return entity;
    }
}
