package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Tag(name = "Channel API", description = "Channel 관련 API")
public interface ChannelApiDocs {

  @Operation(summary = "Public 채널 생성", description = "Public 채널 생성하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Public 채널 생성 성공"),
      @ApiResponse(responseCode = "400", description = "Public 채널 생성 실패")
  })
  ChannelResponse createPublicChannel(
      ChannelRequest.CreatePublic publicChannelRequest);

  @Operation(summary = "Private 채널 생성", description = "Private 채널 생성하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Private 채널 생성 성공"),
      @ApiResponse(responseCode = "404", description = "Private 채널 생성 실패, 해당 유저를 찾을 수 없습니다.")
  })
  ChannelResponse createPrivateChannel(
      ChannelRequest.CreatePrivate privateChannelRequest);

  @Operation(summary = "Public 채널 정보 수정", description = "Public 채널 정보 수정하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Private 채널 수정 성공"),
      @ApiResponse(responseCode = "404", description = "Private 채널 수정 실패, 해당 채널을 찾을 수 없습니다.")
  })
  ChannelResponse updatePublicChannel(
      UUID channelId,
      ChannelRequest.Update publicChannelRequest
  );

  @Operation(summary = "채널 삭제", description = "채널 삭제하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채널 삭제 성공"),
      @ApiResponse(responseCode = "400", description = "채널 삭제 실패")
  })
  String deleteChannel(UUID channelId);

  @Operation(summary = "유저의 채널 리스트", description = "유저가 접근할 수 있는 채널 리스트를 반환합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채널 조회 성공"),
  })
  List<ChannelResponse> getChannelListByUser(UUID userId);
}
