package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
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
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @Operation(summary = "Public Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      )
  })
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @Valid @Parameter(description = "Public Channel 생성 정보") @RequestBody PublicChannelCreateRequest request) {
    ChannelDto channelDto = channelService.createPublicChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 비공개 채널 생성
  @Operation(summary = "Private Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      )
  })
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @Valid @Parameter(description = "Private Channel 생성 정보") @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto channelDto = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 채널 정보 수정
  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel not found: chan{channelId}"))
      ),
      @ApiResponse(
          responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))
      )
  })
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @Parameter(description = "수정할 Channel ID") @PathVariable UUID channelId,
      @Valid @Parameter(description = "수정할 Channel 정보") @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto channelDto = channelService.updatePublicChannel(channelId, request);
    return ResponseEntity.ok(channelDto);
  }

  // 채널 삭제
  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Channel이 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel not found: {channelId}"))
      )
  })
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 Channel ID") @PathVariable UUID channelId) {
    log.info("DELETE /api/channel/{channelId} 요청: channel id = {}", channelId);
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  // 특정 사용자가 접근 가능한 채널 목록 조회
  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
      )
  })
  @GetMapping
  public ResponseEntity<List<ChannelDto>> getChannelsOfUser(
      @Parameter(description = "조회할 User ID") @RequestParam UUID userId) {
    log.info("GET /api/channel 요청: user id = {}", userId);
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}
