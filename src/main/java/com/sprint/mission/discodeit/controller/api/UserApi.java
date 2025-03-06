package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "유저 API")
public interface UserApi {
    @Operation(
            summary = "User 등록",
            operationId = "create",
            tags = {"User"})
    @ApiResponse(
            responseCode = "201",
            description = "User가 성공적으로 생성됨",
            content = @Content(
                    mediaType = "*/*",
                    schema = @Schema(implementation = FindUserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "같은 email 또는 name을 사용하는 User가 이미 존재함",
            content = @Content(
                    mediaType = "*/*",
                    schema = @Schema(type = "string", example = "이미 가입된 이메일입니다.")
            )
    )
    ResponseEntity<FindUserResponseDto> create(CreateUserRequestDto createUserRequestDto) throws IOException;

    @Operation(
            summary = "전체 User 목록 조회",
            operationId = "findAll",
            tags = {"User"}
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 목록 조회 성공",
            content = @Content(
                    mediaType = "*/*",
                    schema = @Schema(implementation = FindUserResponseDto.class)
            )
    )
    ResponseEntity<List<FindUserResponseDto>> findAll();

    @Operation(summary = "유저 수정", description = "유저의 정보를 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "유저 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindUserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "유저 수정 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<FindUserResponseDto> updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto) throws IOException;

    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "유저 조회 성공"
    )
    @ApiResponse(
            responseCode = "404",
            description = "유저 조회 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<Void> deleteUser(UUID id);

    @Operation(summary = "유저 온라인 상태 수정", description = "유저의 온라인 상태를 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "유저 온라인 상태 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindUserResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "유저 수정 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<FindUserResponseDto> updateOnline(UUID id);
}
