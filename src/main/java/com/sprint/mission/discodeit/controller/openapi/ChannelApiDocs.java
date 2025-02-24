package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Channel API", description = "Channel 작업")
public interface ChannelApiDocs {

  @Operation(summary = "Private Channel 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Private Channel 생성 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<PrivateChannelResponseDto> createPrivateChannel(@Parameter(required = true, description = "Private Channel 생성 정보") CreatePrivateChannelDto channelDto);

  @Operation(summary = "Public Channel 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Public Channel 생성 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<PublicChannelResponseDto> createPublicChannel(@Parameter(required = true, description = "Public Channel 생성 정보") CreateChannelDto channelDto);


  @Operation(summary = "Public Channel 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Public Channel 업데이트 성공"),
      @ApiResponse(
          responseCode = "400",
          description = "Private Channel 은 업데이트 할 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 을 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
  })
  ResponseEntity<UpdateChannelResponseDto> updateChannel(
      @Parameter(description = "업데이트 Channel UUID", required = true) String channelId,
      @Parameter(description = "업데이트 정보", required = true) ChannelUpdateDto channelDto
  );


  @Operation(summary = "Channel 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Channel 삭제 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "Channel을 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<Void> deleteChannel(@Parameter(required = true, description = "삭제할 Channel UUID") String channelId);


  @Operation(summary = "User가 조회할 수 있는 Channel 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Channel 조회 성공")
  })
  ResponseEntity<List<FindChannelResponseDto>> findChannelVisibleToUser(@Parameter(required = true, description = "User UUID") String userId);
}
