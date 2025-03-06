package com.sprint.mission.discodeit.controller.user;


import com.sprint.mission.discodeit.dto.request.user.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.user.UserLoginResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "UserLogin Controller", description = "사용자 로그인 관련 API 앤드포인트 관리")
public class UserLoginController {

  private final AuthService authService;

  public UserLoginController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "사용자 로그인", description = "사용자 로그인 처리 앤드 포인트")
  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDTO> login(
      @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
    UserLoginResponseDTO login = authService.login(userLoginRequestDTO);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(login);
  }
}
