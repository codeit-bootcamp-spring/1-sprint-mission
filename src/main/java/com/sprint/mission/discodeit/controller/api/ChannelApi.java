package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "채널 API")
public interface ChannelApi {

    @Operation(summary = "공개 채널 생성", description = "공개 채널을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "공개 채널 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindChannelResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 생성 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<FindChannelResponseDto> createPublicChannel(CreatePublicChannelRequestDto createPublicChannelRequestDto);

    @Operation(summary = "비공개 채널 생성", description = "비공개 채널을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "비공개 채널 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindChannelResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 생성 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<FindChannelResponseDto> createPrivateChannel(UUID ownerId);

    @Operation(summary = "공개 채널 수정", description = "공개 채널을 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "공개 채널 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindChannelResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 수정 실패 - 존재하지 않는 채널이거나 비공개 채널"
    )
    ResponseEntity<FindChannelResponseDto> updatePublicChannel(UpdatePublicChannelRequestDto updatePublicChannelRequestDto);

    @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "채널 삭제 성공"
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 삭제 실패 - 존재하지 않는 채널"
    )
    ResponseEntity<Void> deleteChannel(UUID id);

    @Operation(summary = "채널 다건 조회", description = "해당 유저가 조회할 수 있는 모든 채널을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "채널 다건 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindChannelResponseDto.class)
            )
    )
    ResponseEntity<List<FindChannelResponseDto>> findChannel(UUID userId);
}
