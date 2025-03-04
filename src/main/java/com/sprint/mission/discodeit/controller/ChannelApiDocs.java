package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApiDocs {

  @Operation(summary = "Public Channel 생성", description = "새로운 공개 채널을 생성합니다.")
  ResponseEntity<Channel> createPublic(
      @Parameter(description = "공개 채널 생성 요청 정보", required = true) PublicChannelCreateRequest request
  );

  @Operation(summary = "Private Channel 생성", description = "새로운 비공개 채널을 생성합니다.")
  ResponseEntity<Channel> createPrivate(
      @Parameter(description = "비공개 채널 생성 요청 정보", required = true) PrivateChannelCreateRequest request
  );

  @Operation(summary = "Channel 정보 수정", description = "기존 채널 정보를 수정합니다.")
  ResponseEntity<Channel> update(
      @Parameter(description = "수정할 Channel ID", required = true) UUID channelId,
      @Parameter(description = "채널 수정 요청 정보", required = true) PublicChannelUpdateRequest request
  );

  @Operation(summary = "Channel 삭제", description = "지정된 채널을 삭제합니다.")
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 Channel ID", required = true) UUID channelId
  );

  @Operation(summary = "User가 참여 중인 Channel 목록 조회", description = "특정 사용자가 참여 중인 채널 목록을 조회합니다.")
  ResponseEntity<List<ChannelDto>> findAll(
      @Parameter(description = "조회할 User ID", required = true) UUID userId
  );
}