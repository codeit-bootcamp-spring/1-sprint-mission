package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.auth.LoginDTO;
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

@Tag(name = "Auth", description = "사용자 인증 API")
public interface AuthAPI {

    @Operation(summary = "login")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Login Success",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{username} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "비밀번호가 일치하지 않음",
                    content = @Content(examples = @ExampleObject(value = "UnCorrect Password"))
            )
    })
    ResponseEntity<User> login(
            @Parameter(description = "login Info") LoginDTO loginDTO
    );
}