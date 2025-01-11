package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.USER_NOT_FOUND;

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
        // TODO : 이메일, 또는 이름으로 중복 검사 실시 해야함.
        var savedEntity = data.save(entity);
        return converter.toDto(savedEntity);
    }

    @Override
    public UserInfoResponse findUserByUsername(FindUserRequest findUserRequest) {
        var entity = data.findByUsername(findUserRequest.username())
                .filter(User::isNotUnregistered)
                .map(converter::toDto)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));
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

    @Override
    public void createChannel() {

    }


    private User findById(UUID id) {
        var entity = data.findById(id)
                .filter(User::isNotUnregistered)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        return entity;
    }
}
