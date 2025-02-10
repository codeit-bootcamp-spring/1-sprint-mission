package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {

    User isUserExist(AuthUserDTO authUserDTO);

}
