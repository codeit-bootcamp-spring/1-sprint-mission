package com.sprint.mission.discodeit.controller.api;


import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Channel", description = "채널 관련 API")
@RequestMapping("/api/channels")
public interface ChannelApi {

  @Operation(summary = "채널 생성", description = "새로운 공개 채널을 생성합니다.")
  @ApiResponse(responseCode = "201", description = "채널이 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class)))
  @PostMapping("/public")
  ResponseEntity<ChannelResponse> createChannel(CreateChannelRequest request);

  @Operation(summary = "비공개 채널 생성", description = "새로운 비공개 채널을 생성합니다.")
  @ApiResponse(responseCode = "201", description = "비공개 채널이 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class)))
  @PostMapping("/private")
  ResponseEntity<ChannelResponse> createPrivateChannel(CreatePrivateChannelRequest request);


  @Operation(summary = "전체 채널 조회", description = "모든 채널 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "채널 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class)))
  @GetMapping
  ResponseEntity<List<ChannelResponse>> getChannels();

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class)))
  @GetMapping("/userId")
  ResponseEntity<List<ChannelResponse>> findAll_1(
      @Parameter(description = "검색할 userId") UUID userId
  );

  @Operation(summary = "특정 채널 조회", description = "채널 ID를 이용하여 특정 채널을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채널 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class))),
      @ApiResponse(responseCode = "404", description = "해당 ID의 채널을 찾을 수 없음")})
  @GetMapping("/{id}")
  ResponseEntity<ChannelResponse> getChannel(
      @Parameter(description = "조회할 채널의 ID", required = true) @PathVariable UUID id
  );

  @Operation(summary = "채널 정보 수정", description = "채널의 이름을 변경합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채널 정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelResponse.class))),
      @ApiResponse(responseCode = "404", description = "해당 ID의 채널을 찾을 수 없음")})
  @PatchMapping("/{id}")
  ResponseEntity<ChannelResponse> updateChannel(
      @Parameter(description = "수정할 채널의 ID", required = true) UUID id,
      @RequestBody UpdateChannelRequest request
  );

  @Operation(summary = "채널 삭제", description = "채널 ID를 이용하여 채널을 삭제합니다.")
  @ApiResponses({@ApiResponse(responseCode = "204", description = "채널 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 ID의 채널을 찾을 수 없음")})
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 채널의 ID", required = true) UUID id
  );
}
