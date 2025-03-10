package com.sprint.mission.controller;


//import com.sprint.mission.dto.request.ChannelDtoForRequest;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.FindChannelAllDto;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.service.ChannelService;

import java.util.List;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성")
    @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class)))
    @PostMapping("public")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid PublicChannelCreateDTO request) {
        Channel createdChannel = channelService.createPublicChannel(request);
        return CommonResponse.toResponseEntity
                (CREATED, "Public 채널이 생성되었습니다.", getFindChannelDto(createdChannel));
    }

    @Operation(summary = "Private Channel 생성")
    @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class)))
    @PostMapping("private")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid PrivateChannelCreateDTO request) {
        Channel createdChannel = channelService.createPrivateChannel(request);
        return CommonResponse.toResponseEntity
                (CREATED, "Private 채널이 생성되었습니다.", getFindChannelDto(createdChannel));
    }

    @Operation(summary = "Channel 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = ChannelDtoForUpdate.class))),
            @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @PatchMapping("{id}")
    public ResponseEntity<CommonResponse> update(
            @Parameter(description = "수정할 Channel ID") @PathVariable("id") UUID channelId,
            @RequestBody @Valid ChannelDtoForUpdate requestDTO) {
        channelService.update(channelId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 업데이트되었습니다", requestDTO);
    }

    //[ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.

    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FindChannelAllDto.class)))),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<CommonResponse> findAllByUserId(
            @Parameter(description = "조회할 User ID") @RequestParam("userId") UUID userId) {

        List<FindChannelAllDto> channelDtoList = channelService.findAllByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 조회되었습니다", channelDtoList);
    }

    @Operation(summary = "Channel 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse> delete(
            @Parameter(description = "삭제할 Channel ID") @PathVariable("id") UUID channelId) {

        channelService.delete(channelId);
        return CommonResponse.toResponseEntityWithoutData
                (NO_CONTENT, "성공적으로 삭제되었습니다");
    }

    /**
     * 응답 DTO (타입별)
     */
    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.isPrivate())
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel);
    }
}


