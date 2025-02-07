package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.ErrorCode.USER_NOT_FOUND;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.dto.ExitChannelRequest;
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

    private final UserRepository userRepository;
    private final UserConverter converter;

    public JCFUserService(
            UserRepository userRepository,
            UserConverter converter
    ) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public UserInfoResponse register(RegisterUserRequest registerUserRequest) {
        var entity = converter.toEntity(registerUserRequest);
        var savedEntity = userRepository.save(entity);

        return converter.toDto(savedEntity);
    }

    @Override
    public UserInfoResponse findUserByUsernameOrThrow(FindUserRequest findUserRequest) {
        var foundUser = userRepository.findByUsername(findUserRequest.username())
                .filter(User::isRegistered)
                .map(converter::toDto)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return foundUser;
    }

    @Override
    public UserInfoResponse modifyUserInfo(ModifyUserInfoRequest request) {
        var foundUser = findByUserIdOrThrow(request.id());

        foundUser.changeUserName(request.changeUsername());
        userRepository.save(foundUser);

        var response = converter.toDto(foundUser);
        return response;
    }

    @Override
    public void UnRegisterUser(UnregisterUserRequest request) {
        var foundUser = findByUserIdOrThrow(request.id());

        foundUser.unregister();

        userRepository.save(foundUser);
    }

    @Override
    public void exitChannel(ExitChannelRequest request) {
        var foundUser = findByUserIdOrThrow(request.userId());
        foundUser.exitParticipatedChannel(request.channelId());
        userRepository.save(foundUser);
    }

    private User findByUserIdOrThrow(UUID id) {
        var entity = userRepository.findById(id)
                .filter(User::isRegistered)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        return entity;
    }

    public static JCFUserService getInstance(UserRepository userRepository) {
        return new JCFUserService(userRepository, new UserConverter());
    }
}
