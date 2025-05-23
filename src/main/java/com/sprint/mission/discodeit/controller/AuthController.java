package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserMapper userMapper;

  @GetMapping(path = "csrf-token")
  public ResponseEntity<Map<String,String>>getCsrfToken(CsrfToken csrfToken){
    Map<String,String> token = new HashMap<>();
    token.put("headerName", csrfToken.getHeaderName());
    token.put("parameterName", csrfToken.getParameterName());
    token.put("token", csrfToken.getToken());

    return ResponseEntity.ok(token);
  }

  @GetMapping(path = "/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails principal) {
    if(principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    User user = principal.getUser();
    UserDto userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  @PutMapping(path = "/role")
  public ResponseEntity<UserDto> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest request){
    UserDto userDto = authService.updateUserRole(request);
    return ResponseEntity.ok(userDto);
  }
}
