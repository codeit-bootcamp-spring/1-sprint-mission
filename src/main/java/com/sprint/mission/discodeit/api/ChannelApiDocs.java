package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApiDocs {

  @Operation(summary = "Public Channel 생성", description = "새로운 공개 채널을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
  })
  ResponseEntity<Channel> createPublic(PublicChannelCreateRequest request);

  @Operation(summary = "Private Channel 생성", description = "새로운 비공개 채널을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
  })
  ResponseEntity<Channel> createPrivate(PrivateChannelCreateRequest request);

  @Operation(summary = "Channel 정보 수정", description = "기존의 채널 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음")
  })
  ResponseEntity<Channel> update(UUID channelId, PublicChannelUpdateRequest request);

  @Operation(summary = "Channel 삭제", description = "특정 채널을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음")
  })
  ResponseEntity<Void> delete(UUID channelId);

  @Operation(summary = "User가 참여 중인 Channel 목록 조회", description = "특정 사용자가 참여 중인 채널 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
  })
  ResponseEntity<List<ChannelDto>> findAll(UUID userId);

  ResponseEntity<List<ChannelDto>> findAll();
}
