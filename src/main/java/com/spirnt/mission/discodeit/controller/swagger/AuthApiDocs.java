package com.spirnt.mission.discodeit.controller.swagger;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApiDocs {

  // 로그인
  @Operation(summary = "로그인", operationId = "login")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그인 성공",
          content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "User with username {username} not found"))),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음",
          content = @Content(examples = @ExampleObject(value = "Wrong password")))
  })
  ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest);
}
