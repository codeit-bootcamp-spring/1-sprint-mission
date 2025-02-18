package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.LoginRequestDTO;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.jcf.JCFAuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class BasicAuthService {
    private final AuthService jcAuthService;

    public BasicAuthService(@Qualifier("JCFAuthService") AuthService jcAuthService){
        this.jcAuthService=jcAuthService;
    }

    @PostMapping("/login")
    public boolean login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return jcAuthService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }

}
