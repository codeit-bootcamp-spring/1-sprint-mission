package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channels", description = "채널 관련 정보")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Operation(
      summary = "공개 채널 생성",
      description = "새로운 공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "공개 채널 생성 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelResponse.class)
              )
          )
      }
  )
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody ChannelCreateRequest request) {
    ChannelResponse createdChannel = channelService.createPublicChannel(
        request.name(),
        request.description());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PostMapping("/private")
  @Operation(
      summary = "비공개 채널 생성",
      description = "새로운 비공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "비공개 채널 생성 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelResponse.class)
              )
          )
      }
  )
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody ChannelCreateRequest request) {
    ChannelResponse createdChannel = channelService.createChannel(
        request.name(),
        request.description(),
        ChannelType.PRIVATE);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @GetMapping("/{channelId}")
  @Operation(
      summary = "채널 ID로 조회",
      description = "특정 ID의 채널을 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "채널 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "채널을 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Channel with id {channelId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<ChannelResponse> getChannelById(
      @Parameter(description = "채널 ID로 채널 정보 출력")
      @PathVariable("channelId") UUID channelId) {
    ChannelResponse channel = channelService.getChannelById(channelId);
    if (channel == null) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channel);
  }

  @GetMapping
  @Operation(
      summary = "전체 채널 조회",
      description = "모든 채널을 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "채널 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelResponse.class)
              )
          )
      }
  )
  public ResponseEntity<List<ChannelResponse>> getAllChannels() {
    List<ChannelResponse> channels = channelService.getAllChannels();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }

  @PutMapping("/{channelId}")
  @Operation(
      summary = "채널 수정",
      description = "기존 채널을 수정합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "채널 수정 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "채널을 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Channel with id {channelId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<ChannelResponse> updateChannel(
      @Parameter(description = "채널 ID로 정보 수정")
      @PathVariable("channelId") UUID channelId,
      @RequestBody ChannelUpdateRequest request) {
    ChannelResponse updatedChannel = channelService.updateChannel(channelId, request);
    if (updatedChannel == null) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  @DeleteMapping("/{channelId}")
  @Operation(
      summary = "채널 삭제",
      description = "특정 채널을 삭제합니다.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "채널 삭제 성공"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "채널을 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Channel with id {channelId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> deleteChannel(
      @Parameter(description = "채널 ID로 채널 삭제")
      @PathVariable("channelId") UUID channelId) {
    boolean deleted = channelService.deleteChannel(channelId);
    if (deleted) {
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } else {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
  }
}