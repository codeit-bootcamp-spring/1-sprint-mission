package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth/login")
@Tag(name = "Login API" , description = "로그인 관리 API")
@AllArgsConstructor
public class AuthRestController {
    private final AuthService authService;

    @PostMapping
    public boolean login(@RequestBody @JsonProperty UserRequest request){
        log.info("login request : {}", request);
        return authService.login(request.username(), request.password());
    }
}
