package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
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
                .map(converter::toDto)
                .orElseThrow();
        return entity;
    }

    @Override
    public void modifyUserInfo() {

    }

    @Override
    public void UnRegisterUser() {

    }

}
