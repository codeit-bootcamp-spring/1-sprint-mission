package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageApiDocs {

  @Operation(summary = "Message 생성", description = "새로운 메시지를 생성합니다.")
  ResponseEntity<Message> create(
      @Parameter(description = "메시지 생성 요청 정보", required = true) MessageCreateRequest messageCreateRequest,
      @Parameter(description = "메시지 첨부 파일들", required = false) List<MultipartFile> attachments
  );

  @Operation(summary = "Message 내용 수정", description = "기존 메시지의 내용을 수정합니다.")
  ResponseEntity<Message> update(
      @Parameter(description = "수정할 Message ID", required = true) UUID messageId,
      @Parameter(description = "수정할 메시지 내용", required = true) MessageUpdateRequest request
  );

  @Operation(summary = "Message 삭제", description = "지정된 메시지를 삭제합니다.")
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 Message ID", required = true) UUID messageId
  );

  @Operation(summary = "Channel의 Message 목록 조회", description = "특정 채널의 메시지 목록을 조회합니다.")
  ResponseEntity<List<Message>> getAllByChannelId(
      @Parameter(description = "조회할 Channel ID", required = true) UUID channelId
  );
}