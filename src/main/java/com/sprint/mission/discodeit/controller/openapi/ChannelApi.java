package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
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

@Tag(name = "Channel", description = "Channel Api")
public interface ChannelApi {

  @Operation(summary = "공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Public Channel Creation Successful",
          content = @Content(schema = @Schema(implementation = Channel.class))
      )
  })
  ResponseEntity<Channel> create(
      @Parameter(description = "Public 채널 생성 정보") PublicChannelCreateRequest request);


  @Operation(summary = "비공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Private Channel Creation Successful",
          content = @Content(schema = @Schema(implementation = Channel.class))
      )
  })
  ResponseEntity<Channel> create(
      @Parameter(description = "Private 채널 생성 정보") PrivateChannelCreateRequest request);


  @Operation(summary = "채널 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel information update successful.",
          content = @Content(schema = @Schema(implementation = Channel.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel not found.",
          content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "Private channel cannot be updated.",
          content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))
      )
  })
  ResponseEntity<Channel> update(
      @Parameter(description = "대상 채널의 ID") UUID channelId,
      @Parameter(description = "수정할 내용") PublicChannelUpdateRequest request);


  @Operation(summary = "채널 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Channel Deletion Successful"
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel not found.",
          content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
      )
  })
  ResponseEntity<Void> delete(
      @Parameter(description = "대상 채널 ID") UUID channelId
  );

  @Operation(summary = "유저가 참여중인 채널 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel list lookup successful",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
      )
  })
  ResponseEntity<List<ChannelDto>> findAll(
      @Parameter(description = "대상 User ID") UUID userId
  );
}
