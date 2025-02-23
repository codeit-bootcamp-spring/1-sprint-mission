package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  
  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public ResponseEntity<FindUserDto> login(@RequestParam String username, @RequestParam String password) {
    FindUserDto findUserDto = authService.login(username, password);
    return ResponseEntity.ok(findUserDto);
  }
}
