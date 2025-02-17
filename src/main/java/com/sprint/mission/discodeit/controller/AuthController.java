package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<UserResponseDto> userLogin(@RequestBody LoginDto loginDto){
    UserResponseDto user = authService.login(loginDto.username(), loginDto.password());
    return ResponseEntity.ok(user);
  }

}
