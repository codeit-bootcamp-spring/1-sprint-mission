package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {

    User isUserExist(AuthUserDTO authUserDTO);

}
