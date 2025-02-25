package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.users.login.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){
        User user = authService.login(loginRequest);
        return ResponseEntity.ok(user);
    }
}
