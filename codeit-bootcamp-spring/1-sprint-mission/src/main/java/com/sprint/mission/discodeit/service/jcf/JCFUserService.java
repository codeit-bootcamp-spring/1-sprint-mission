package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.USER_NOT_FOUND;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.user.UserConverter;
import com.sprint.mission.discodeit.service.user.UserService;

public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserConverter converter;

    public JCFUserService(UserRepository userRepository, UserConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public void register(RegisterUserRequest registerUserRequest) {
        var entity = converter.toEntity(registerUserRequest);
        userRepository.save(entity);
    }

    @Override
    public UserInfoResponse findUserByUsername(FindUserRequest findUserRequest) {
        var entity = userRepository.findByUsername(findUserRequest.username())
                .filter(user -> user.getStatus() != UNREGISTERED)
                .map(converter::toDto)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage()));
        return entity;
    }

    @Override
    public void modifyUserInfo(ModifyUserInfoRequest request) {
        var entity = userRepository.findById(request.id())
                .filter(user -> user.getStatus() != UNREGISTERED)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage()));

        entity.changeName(request.changeUsername());

        userRepository.save(entity);
    }

    @Override
    public void UnRegisterUser(UnregisterUserRequest request) {
        var entity = userRepository.findById(request.id())
                .filter(user -> user.getStatus() != UNREGISTERED)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage()));

        entity.unregister();

        userRepository.save(entity);
    }

}
