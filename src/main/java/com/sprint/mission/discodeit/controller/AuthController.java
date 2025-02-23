package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.authDto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.userDto.FindUserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FindUserResponseDto> login(@RequestParam String name,
                                                     @RequestParam String password) {
        LoginRequestDto loginRequestDto = new LoginRequestDto(name, password);
        FindUserResponseDto findUserDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(findUserDto);
    }
}
