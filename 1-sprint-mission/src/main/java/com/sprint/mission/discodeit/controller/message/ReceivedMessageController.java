package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.status.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.request.status.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/received_messages")
public class ReceivedMessageController {

  private final ReadStatusService readStatusService;

  public ReceivedMessageController(ReadStatusService readStatusService) {
    this.readStatusService = readStatusService;
  }

  //특정 채널의 메시지 수신 정보 생성
  @PostMapping
  public ResponseEntity<ReadStatusResponseDTO> createReadStatusMessage(
      @RequestBody ReadStatusCreateDTO createDTO) {
    ReadStatusResponseDTO readStatusResponseDTO = readStatusService.create(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusResponseDTO);
  }

  //특정 채널의 메시지 수신 정보 수정
  @PatchMapping("/modify")
  public ResponseEntity<ReadStatusResponseDTO> modifyReadStatusMessage(
      @RequestBody ReadStatusUpdateDTO updateDTO) {
    ReadStatusResponseDTO update = readStatusService.update(updateDTO);
    return ResponseEntity.ok(update);
  }

  //특정 사용자의 메시지 수신 정보 조회
  @GetMapping("/{userId}")
  public ResponseEntity<List<ReadStatusResponseDTO>> getReadStatusUser(@PathVariable UUID userId) {
    List<ReadStatusResponseDTO> statusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statusList);
  }


}
