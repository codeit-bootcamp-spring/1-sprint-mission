package com.sprint.mission.discodeit.apidocs;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApiDocs {

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject("Channel | User with id {channelId | userId} not found")
          )
      ),
      @ApiResponse(responseCode = "400",
          description = "이미 읽음 상태가 존재함",
          content = @Content(
              examples = @ExampleObject("ReadStatus with userId {userId} and channelId {channelId} already exists")
          )
      ),
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨")
  })
  ResponseEntity<ReadStatus> create(
      @Valid @RequestBody ReadStatusCreateRequest readStatusCreateRequest);

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
  ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(description = "조회할 User ID") @RequestParam UUID userId);


  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404",
          description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject("ReadStatus with id {readStatusId} not found")
          )
      )
  })
  ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 읽음 상태 ID") @PathVariable UUID readStatusId,
      @Valid @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest);

}
