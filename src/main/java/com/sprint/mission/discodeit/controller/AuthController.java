package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Login", description = "관리자 로그인 관련 정보")
public class AuthController {

  private final AuthService authService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @Operation(
      summary = "유저 로그인",
      description = "로그인",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "로그인 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "401",
              description = "로그인 실패",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Wrong password"
                  )
              )
          )
      }
  )
  public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
    UserResponse userResponse = authService.login(loginRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userResponse);
  }
}