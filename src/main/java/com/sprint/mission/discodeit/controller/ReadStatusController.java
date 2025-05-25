package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
@Tag(name = "ReadStatus", description = "메세지 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  //개별 조회
//  @GetMapping("/{readStatusId}")
//  public ReadStatusResponseDto getReadStatus(@PathVariable String readStatusId) {
//    return readStatusService.findById(readStatusId);
//  }

  //개별 생성
  @PreAuthorize("authentication.principal.userId.toString() == #createReadStatusDto.userId()")
  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @Valid @RequestBody CreateReadStatusDto createReadStatusDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.create(createReadStatusDto));
  }

  @PreAuthorize("hasPermission(#readStatusId, 'ReadStatus', 'UPDATE')")
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable String readStatusId,
      @Valid @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    ReadStatusDto updatedReadStatusDto = readStatusService.update(readStatusId,
        updateReadStatusDto);
    return ResponseEntity.ok().body(updatedReadStatusDto);
  }

  //특정 사용자의 메세지 수신 정보 조회
  @GetMapping
  public List<ReadStatusDto> getReadStatusByUserId(@RequestParam String userId) {
    return readStatusService.findAllByUserId(userId);
  }

  //특정 채널의 메세지 수신 정보 수정
  @PreAuthorize("hasPermission(#channelId, 'ReadStatus', 'UPDATE_CHANNEL')")
  @PatchMapping
  public ResponseEntity<List<ReadStatusDto>> updateChannelReadStatus(
      @RequestParam String channelId,
      @Valid @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    return ResponseEntity.ok(readStatusService.updateByChannelId(channelId, updateReadStatusDto));
  }
}
