package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.LoginRequestDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("JCFAuthService")
public class JCFAuthService implements AuthService {

    private final JCFUserService userService;
    public JCFAuthService(@Qualifier("FileUserService") JCFUserService userService){
        this.userService=userService;
    }
    @Override
    public UserDTO login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.username();
        String password = loginRequestDTO.password();


        User user = userService.findUser(username)
                .values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 로그인 정보입니다."));


        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("유효하지 않은 로그인 정보입니다.");
        }

        return UserDTO.fromEntity(user);
    }
}
