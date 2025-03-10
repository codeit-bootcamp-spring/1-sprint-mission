package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Channel", description = "Channel API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            )
    })
    @PostMapping(path = "public")
    public ResponseEntity<Channel> create(@Parameter(description = "Public Channel 생성 정보") @RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.createChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }

    @Operation(summary = "Private Channel 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            )
    })
    @PostMapping(path = "private")
    public ResponseEntity<Channel> create(@Parameter(description = "Private Channel 생성 정보") @RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.createChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }

    @Operation(summary = "Channel 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel을 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Private Channel은 수정할 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))
            )
    })
    @PatchMapping(path = "{channelId}")
    public ResponseEntity<Channel> update(@Parameter(description = "수정할 Channel ID") @PathVariable("channelId") UUID channelId, @Parameter(description = "수정할 Channel 정보") @RequestBody PublicChannelUpdateRequest request) {
        Channel channel = channelService.updateChannelField(channelId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channel);
    }

    @Operation(summary = "Channel 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Channel이 성공적으로 삭제됨"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel을 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
            )
    })
    @DeleteMapping(path = "{channelId}")
    public ResponseEntity<Void> delete(@Parameter(description = "삭제할 Channel ID") @PathVariable("channelId") UUID channelId) {
        channelService.deleteChannelById(channelId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channel 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
            )
    })
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(@Parameter(description = "조회할 User ID") @RequestParam("userId") UUID userId) {
        List<ChannelDto> channelDtoList = channelService.readAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channelDtoList);
    }

}
