package com.sprint.mission.discodeit.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

//  @Operation(summary = "CSRF 토큰 발급")
//  @ApiResponses(value = {
//      @ApiResponse(
//          responseCode = "200", description = "발급 성공",
//          content = @Content(schema = @Schema(implementation = CsrfToken.class))
//      )
//  })
//  ResponseEntity<CsrfToken> getCsrfToken(@Parameter(hidden = true) CsrfToken csrfToken);
//
//  @Operation(summary = "세션을 활용한 현재 사용자 정보 조회")
//  @ApiResponses(value = {
//      @ApiResponse(
//          responseCode = "200", description = "조회 성공",
//          content = @Content(schema = @Schema(implementation = UserDto.class))
//      ),
//      @ApiResponse(
//          responseCode = "401", description = "인증되지 않은 세션"
//      )
//  })
//  ResponseEntity<UserDto> me(@Parameter(hidden = true) DiscodeitUserDetails userDetails);
//
//  @Operation(summary = "사용자 권한 수정")
//  @ApiResponses(value = {
//      @ApiResponse(
//          responseCode = "200", description = "권한 변경 성공",
//          content = @Content(schema = @Schema(implementation = UserDto.class))
//      )
//  })
//  ResponseEntity<UserDto> role(@Parameter(description = "권한 수정 요청 정보") RoleUpdateRequest request);
} 