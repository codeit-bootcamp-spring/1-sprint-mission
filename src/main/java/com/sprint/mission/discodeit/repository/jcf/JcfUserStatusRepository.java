package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.form.CheckUserDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.UUID;

public class JcfUserStatusRepository {
    private UserStatus userStatus;
    private User user;

    public void createUserStatus(CheckUserDto checkUser) {
        if (checkUser.getLoginId() == null) {
            throw new IllegalStateException("유저를 찾을 수 없습니다.");
        }
        else if(checkUser.getLoginId().equals(user.getLoginId())) {
            throw new IllegalStateException("이미 유저가 존재합니다.");
        }

    }

}
