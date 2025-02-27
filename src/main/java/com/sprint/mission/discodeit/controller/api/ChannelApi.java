package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel", description = "채널 API")
public interface ChannelApi {

    @Operation(
            summary = "public 채널 생성",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PublicChannelCreateRequest.class)
                    ),
                    required = true
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Channel.class))
                    )
            }
    )
    ResponseEntity<Channel> create(PublicChannelCreateRequest request);

    @Operation(
            summary = "Private 채널 생성",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PrivateChannelCreateRequest.class)
                    ),
                    required = true
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Channel.class))
                    )
            }
    )
    ResponseEntity<Channel> create(PrivateChannelCreateRequest request);

    @Operation(
            summary = "채널 정보 수정",
            parameters = @Parameter(
                    name = "channelId",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    required = true
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PublicChannelUpdateRequest.class)
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Channel.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
                    )
            }
    )
    ResponseEntity<Channel> update(UUID channelId, PublicChannelUpdateRequest request);

    @Operation(
            summary = "채널 삭제",
            parameters = @Parameter(
                    required = true,
                    name = "channelId",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found")))
            }
    )
    ResponseEntity<Void> delete(UUID channelId);

    @Operation(
            summary = "유저가 참여한 채널 조회",
            parameters = @Parameter(
                    name = "userId",
                    required = true,
                    description = "조회할 User ID",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ChannelDto.class)))
    ResponseEntity<List<ChannelDto>> findAll(UUID userId);
}
