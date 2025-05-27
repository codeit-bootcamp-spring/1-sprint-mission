package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

    @Operation(summary = "CSRF 토큰 발급")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "CSRF 토큰 발급 성공",
            content = @Content(schema = @Schema(implementation = CsrfTokenDto.class))
        ),
        @ApiResponse(
            responseCode = "500", description = "CSRF 토큰 발급 실패",
            content = @Content(examples = @ExampleObject(value = "Internal server error"))
        )
    })
    ResponseEntity<CsrfTokenDto> getCsrfToken(HttpServletRequest request);

    @Operation(summary = "현재 로그인한 사용자 정보 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(
            responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(examples = @ExampleObject(value = "Unauthorized"))
        )
    })
    ResponseEntity<UserDto> getCurrentUser();
} 