package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message API", description = "메세지 관리 API")
public interface MessageControllerDocs {

  @Operation(summary = "메세지 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "메세지 생성 성공"),
      @ApiResponse(responseCode = "404", description = "채널 혹은 유저를 찾을 수 없음")
  })
  @PostMapping
  ResponseEntity<MessageDto> createMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments
  );

  @Operation(summary = "채널의 모든 메세지 조회")
  @ApiResponse(responseCode = "200", description = "메세지 목록 조회 성공")
  @GetMapping
  ResponseEntity<PageResponse<MessageDto>> getMessages(
      @RequestParam UUID channelId,
      @RequestParam Instant cursor,
      Pageable pageable
  );

  @Operation(summary = "메세지 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메세지 수정 성공"),
      @ApiResponse(responseCode = "404", description = "메세지를 찾을 수 없음")
  })
  @PatchMapping("/{id}")
  ResponseEntity<MessageDto> updateMessage(
      @PathVariable UUID id,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  );

  @Operation(summary = "메세지 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "메세지 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "메세지를 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteMessage(
      @PathVariable UUID id
  );
}
