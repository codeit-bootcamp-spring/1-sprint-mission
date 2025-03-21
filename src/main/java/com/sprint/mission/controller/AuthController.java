package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.dto.request.LoginRequest;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.*;

@Controller("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    //[ ] username, password과 일치하는 유저가 있는지 확인합니다.
    //[ ] 일치하는 유저가 있는 경우: 유저 정보 반환
    //[ ] 일치하는 유저가 없는 경우: 예외 발생
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    private final AuthService authService;

    @RequestMapping("login")
    public ResponseEntity<CommonResponse> login(LoginRequest request) {
        User user = authService.login(request);
        return CommonResponse.toResponseEntity
                (OK, "로그인 성공", user);
    }
}
