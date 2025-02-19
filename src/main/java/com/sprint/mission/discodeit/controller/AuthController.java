package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.dto.auth.AuthLoginDTO;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AuthLoginDTO request){
        authService.login(request);
        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message("로그인 성공")
                .build();
    }
}
