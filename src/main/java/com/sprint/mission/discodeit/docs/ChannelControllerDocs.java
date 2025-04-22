package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channels", description = "Channel 관련 API")
public interface ChannelControllerDocs {

    @Operation(summary = "공개 채널 생성", description = "새로운 공개 채널을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공개 채널 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<ChannelResponseDto> createPublicChannel(
            @Parameter(description = "공개 채널 생성 요청 객체", required = true) @RequestBody PublicChannelRequestDto channelReqDto);

    @Operation(summary = "비공개 채널 생성", description = "새로운 비공개 채널을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비공개 채널 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<ChannelResponseDto> createPrivateChannel(
            @Parameter(description = "비공개 채널 생성 요청 객체", required = true) @RequestBody PrivateChannelRequestDto channelReqDto);

    @Operation(summary = "채널 업데이트", description = "주어진 채널 ID의 채널 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 업데이트 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<ChannelResponseDto> updatePublicChannel(
            @Parameter(description = "채널 ID", required = true) @PathVariable UUID channelId,
            @Parameter(description = "채널 업데이트 요청 객체", required = true) @RequestBody ChannelUpdateDto updateDto);

    @Operation(summary = "채널 삭제", description = "주어진 채널 ID의 채널을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<ChannelResponseDto> deleteChannel(
            @Parameter(description = "채널 ID", required = true) @PathVariable UUID channelId);

    @Operation(summary = "채널 조회", description = "주어진 유저 ID에 해당하는 채널 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<List<ChannelResponseDto>> getChannels(
            @Parameter(description = "유저 ID", required = true) @RequestParam UUID userId);
}
