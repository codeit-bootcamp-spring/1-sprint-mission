package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.db.user.UserRepositoryInMemory;
import com.sprint.mission.discodeit.entity.user.dto.ExitChannelRequest;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public interface UserService {

    UserInfoResponse register(RegisterUserRequest registerUserRequest);

    UserInfoResponse findUserByUsernameOrThrow(FindUserRequest findUserRequest);

    UserInfoResponse modifyUserInfo(ModifyUserInfoRequest request);

    void UnRegisterUser(UnregisterUserRequest request);

    // TODO 유저가 참여한 채널을 나가는 기능
    void exitChannel(ExitChannelRequest request);

    public static UserService getJCFUserService(UserRepository userRepository) {
        return JCFUserService.getInstance(userRepository);
    }

    public static UserService getUserServiceInstance(UserRepository userRepository) {

        if (userRepository instanceof UserRepositoryInMemory) {
            return JCFUserService.getInstance(userRepository);
        }

        return null;
    }
}
