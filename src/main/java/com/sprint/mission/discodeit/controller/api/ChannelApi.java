package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApi {

  @Operation(summary = "Public Channel 생성", description = "새로운 공개 채널을 생성합니다.")
  ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request);

  @Operation(summary = "Private Channel 생성", description = "새로운 비공개 채널을 생성합니다.")
  ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request);

  @Operation(
      summary = "Channel 정보 수정",
      description = "특정 채널의 정보를 업데이트합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Channel 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<ChannelDto> update(@Parameter(description = "수정할 채널 ID", required = true)
      @PathVariable("id") UUID id,
      @RequestBody PublicChannelUpdateRequest request);

  @Operation(
      summary = "Channel 삭제",
      description = "특정 채널을 삭제합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Channel 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 채널 ID", required = true)
  @PathVariable("id") UUID id);

  @Operation(summary = "User가 참여 중인 Channel 목록 조회", description = "사용자가 참여 중인 채널 목록을 반환합니다.")
  ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId);

}
