package com.sprint.mission.discodeit.controller.user;


import com.sprint.mission.discodeit.dto.request.user.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.user.UserLoginResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class UserLoginController {

  private final AuthService authService;

  public UserLoginController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity<UserLoginResponseDTO> login(
      @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
    UserLoginResponseDTO login = authService.login(userLoginRequestDTO);
    return ResponseEntity.ok(login);
  }
}
