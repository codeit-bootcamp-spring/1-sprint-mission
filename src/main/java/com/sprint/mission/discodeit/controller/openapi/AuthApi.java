package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Aut", description = "유저 로그인 API")
public interface AuthApi {

  @Operation(summary = "로그인 요청")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Login successful",
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "User not found",
          content = @Content(examples = @ExampleObject(value = "User with username {username} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "Wrong password",
          content = @Content(examples = @ExampleObject(value = "Wrong password"))
      )
  })
  ResponseEntity<User> login(
      @Parameter(description = "로그인 정보") LoginRequest loginRequest);
}
