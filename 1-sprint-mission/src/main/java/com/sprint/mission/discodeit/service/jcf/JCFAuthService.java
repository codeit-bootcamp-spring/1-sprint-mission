package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.LoginRequestDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("JCFAuthService")
public class JCFAuthService implements AuthService {

    private final UserService userService;
    public JCFAuthService(@Qualifier("FileUserService") UserService userService){
        this.userService=userService;
    }
    @Override
    public boolean login(String name,String password) {

        List<User> user = userService.readUser(name);
        if (user != null) {
            if (user.get(0).checkPassword(password)) {

                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
