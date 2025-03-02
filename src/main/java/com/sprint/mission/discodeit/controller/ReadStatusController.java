package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "Read Statuses", description = "읽음 상태 정보")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Operation(
      summary = "읽음 상태 생성",
      description = "사용자와 채널에 대한 새로운 읽음 상태를 생성합니다.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "읽음 상태 생성 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusResponse.class)
              )
          )
      }
  )
  public ResponseEntity<ReadStatusResponse> create(
      @RequestBody ReadStatusCreateRequest request
  ) {
    ReadStatus createdReadStatus = readStatusService.create(
        request.userId(),
        request.channelId(),
        request.lastReadAt()
    );

    ReadStatusResponse response = ReadStatusResponse.from(createdReadStatus);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @GetMapping
  @Operation(
      summary = "사용자별 읽음 상태 조회",
      description = "특정 사용자의 모든 읽음 상태를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "읽음 상태 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusResponse.class)
              )
          )
      }
  )
  public ResponseEntity<List<ReadStatusResponse>> findAllByUserId(
      @Parameter(description = "읽음 상태를 조회할 사용자 ID")
      @RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    List<ReadStatusResponse> responses = new ArrayList<>();
    for (ReadStatus readStatus : readStatuses) {
      responses.add(ReadStatusResponse.from(readStatus));
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responses);
  }

  @PatchMapping("/{readStatusId}")
  @Operation(
      summary = "읽음 상태 수정",
      description = "특정 읽음 상태를 수정합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "읽음 상태 수정 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "읽음 상태를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "ReadStatus with id {readStatusId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<ReadStatusResponse> update(
      @Parameter(description = "수정할 읽음 상태의 ID")
      @PathVariable("readStatusId") UUID id,
      @RequestBody ReadStatusUpdateRequest request
  ) {
    ReadStatus updatedReadStatus = readStatusService.update(id, request);
    if (updatedReadStatus == null) {
      throw new NoSuchElementException("ReadStatus with id " + id + " not found");
    }
    ReadStatusResponse response = ReadStatusResponse.from(updatedReadStatus);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }
}