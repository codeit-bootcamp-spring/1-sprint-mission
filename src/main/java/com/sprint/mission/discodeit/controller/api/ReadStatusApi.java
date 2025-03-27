package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApi {

    @Operation(summary = "Message 읽음 상태 생성",
            operationId = "create")
    @ApiResponse(
            responseCode = "201",
            description = "read status 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReadStatusDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "read status 생성 실패 - 존재하지 않는 유저나 채널"
    )
    ResponseEntity<ReadStatusDto> create(CreateReadStatusRequestDto createReadStatusRequestDto);

    @Operation(summary = "User의 Message 읽음 상태 목록 조회",
            operationId = "findAllByUserId"
    )
    @Parameters(
            @Parameter(name = "userId"
                    , in = ParameterIn.QUERY
                    , description = "조회할 User ID"
                    , required = true
                    , schema = @Schema(type = "string", format = "uuid"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "read status 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReadStatusDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "read status 생성 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<List<ReadStatusDto>> findAllByUserId(UUID userId);

    @Operation(summary = "read status 수정", description = "read status를 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "read status 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReadStatusDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "read status 생성 실패 - 존재하지 않는 read status"
    )
    ResponseEntity<ReadStatusDto> updateReadStatus(UUID id);
}
