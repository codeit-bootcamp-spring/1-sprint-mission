package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ResultDTO;
import com.sprint.mission.discodeit.dto.auth.AuthLoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {
    //[ ] 사용자는 로그인할 수 있다.

    private final AuthService authService;

    @PostMapping("/login")
    public ResultDTO login(@RequestBody AuthLoginDTO request){
        authService.login(request);
        return ResultDTO.builder()
                .code(HttpStatus.OK.value())
                .message("로그인 성공")
                .build();
    }
}
