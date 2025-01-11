package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;

public interface UserService {

    UserInfoResponse register(RegisterUserRequest registerUserRequest);

    UserInfoResponse findUserByUsername(FindUserRequest findUserRequest);

    UserInfoResponse modifyUserInfo(ModifyUserInfoRequest request);

    void UnRegisterUser(UnregisterUserRequest request);

    void createChannel();
}
