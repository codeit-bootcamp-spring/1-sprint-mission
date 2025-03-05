package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//28683da4-1c3a-44d5-952b-40e6802472e2
//aa1c1fcc-7394-4e36-a18c-7db489e3e01c

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/read-status")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ObjectMapper objectMapper;
  private final ReadStatusService readStatusService;

  @Operation(summary = "Message 읽음 상태 생성", description = "새로운 읽음 상태를 생성합니다.")
  @PostMapping
  public ResponseEntity<ReadStatus> create(@RequestBody String jsonRequest)
      throws JsonProcessingException {
    ReadStatusCreateRequest request = objectMapper.readValue(jsonRequest,
        ReadStatusCreateRequest.class);
    ReadStatus createdReadStatus = readStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }


  @Operation(
      summary = "Message 읽음 상태 수정",
      description = "특정 Message 읽음 상태를 업데이트합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 업데이트 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PatchMapping("/{id}")
  public ResponseEntity<ReadStatus> update(@Parameter(description = "수정할 읽음 상태 ID", required = true)
      @PathVariable("id") UUID id,
      @RequestBody String jsonRequest) throws JsonProcessingException {
    ReadStatusUpdateRequest request = objectMapper.readValue(jsonRequest,
        ReadStatusUpdateRequest.class);
    ReadStatus updatedReadStatus = readStatusService.update(id, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @Operation(summary = "User의 Message 읽음 상태 목록 조회", description = "사용자의 읽음 상태를 반환합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }
}
