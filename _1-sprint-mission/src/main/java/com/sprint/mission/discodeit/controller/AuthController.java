package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(path = "login")
    public ResponseEntity<User> login(@RequestBody LoginDto loginDto){
        User loginUser = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }
}
