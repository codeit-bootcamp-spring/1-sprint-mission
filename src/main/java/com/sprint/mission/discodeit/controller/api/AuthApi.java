package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "로그인 API 입니다.")
public interface AuthApi {

    @Operation(
            summary = "로그인",
            description = "유저 이름과 비밀번호로 로그인 처리",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(
                                    name = "request",
                                    implementation = LoginRequest.class
                            )
                    )
            )
    )
    @ApiResponses(
            {@ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "User name not found",
                    content = @Content(schema = @Schema(examples = "User name not found", implementation = String.class)))}
    )
    ResponseEntity<User> login(LoginRequest request);

}
