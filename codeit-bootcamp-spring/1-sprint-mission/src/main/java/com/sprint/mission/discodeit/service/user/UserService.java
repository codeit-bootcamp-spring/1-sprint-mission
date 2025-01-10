package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;

public interface UserService {

    // 회원 가입
    void register(RegisterUserRequest registerUserRequest);

    // 이름으로 객체 가져오기
    UserInfoResponse findUserByUsername(FindUserRequest findUserRequest);

    // 회원 정보 수정하기
    void modifyUserInfo(ModifyUserInfoRequest request);

    // 회원 탈퇴 하기
    void UnRegisterUser(UnregisterUserRequest request);
}
