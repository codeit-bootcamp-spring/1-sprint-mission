package com.spirnt.mission.discodeit.controller.swagger;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Channel", description = "Channel API")
public interface ChannelApiDocs {

  // 공개 채널 생성
  @Operation(summary = "Public Channel 생성", operationId = "create_3")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class)))
  })
  ResponseEntity<ChannelDto> creatChannelPublic(
      @RequestBody PublicChannelCreateRequest request);

  // 비공개 채널 생성
  @Operation(summary = "Private Channel 생성", operationId = "create_4")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class)))
  })
  ResponseEntity<ChannelDto> createChannelPrivate(@RequestBody PrivateChannelCreateRequest request);

  // 공개 채널 정보 수정
  @Operation(summary = "Channel 정보 수정", operationId = "update_3")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found")))
  })
  ResponseEntity<ChannelDto> updatePublicChannel(
      @Parameter(name = "channelId", description = "수정할 Channel ID") @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request);

  // 채널 삭제
  @Operation(summary = "Channel 삭제", operationId = "delete_2")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(examples = @ExampleObject("Channel with id {channelId} not found")))
  })
  ResponseEntity<Void> deleteChannel(
      @Parameter(name = "channelId", description = "삭제할 Channel ID") @PathVariable UUID channelId);

  // 특정 사용자가 볼 수 있는 채널 목록 조회
  @Operation(summary = "User가 참여 중인 Channel 목록 조회", operationId = "findAll_1")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class))))
  })
  ResponseEntity<List<ChannelDto>> getAllChannelsByUserId(
      @Parameter(name = "userId", description = "조회할 User ID") @RequestParam UUID userId);
}
