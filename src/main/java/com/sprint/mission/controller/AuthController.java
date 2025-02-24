package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.dto.request.LoginRequest;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    //[ ] username, password과 일치하는 유저가 있는지 확인합니다.
    //[ ] 일치하는 유저가 있는 경우: 유저 정보 반환
    //[ ] 일치하는 유저가 없는 경우: 예외 발생
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    private final AuthService authService;

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    @GetMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid LoginRequest request) {
        User user = authService.login(request);
        return CommonResponse.toResponseEntity
                (OK, "로그인 성공", user);
    }
}
