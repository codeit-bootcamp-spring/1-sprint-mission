package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.status.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.request.status.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/received-messages")
@Tag(name = "Received Message Controller", description = "메세지 수신 정보 관련 API 앤드포인트 관리")
public class ReceivedMessageController {

  private final ReadStatusService readStatusService;

  public ReceivedMessageController(ReadStatusService readStatusService) {
    this.readStatusService = readStatusService;
  }

  @Operation(summary = "특정 채널 메세지 수신 정보 등록", description = " 특정 채널 새로운 메세지 수신 정보 등록 및 메세지 수신 정보 반환")
  @PostMapping
  public ResponseEntity<ReadStatusResponseDTO> createReadStatusMessage(
      @RequestBody ReadStatusCreateDTO createDTO) {
    ReadStatusResponseDTO readStatusResponseDTO = readStatusService.create(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusResponseDTO);
  }

  //특정 채널의 메시지 수신 정보 수정
  @Operation(summary = "특정 채널 메세지 수신 정보 수정", description = " 특정 채널 메세지 수신 정보 수정 및  메세지 수신 정보 반환")
  @PatchMapping("/modify")
  public ResponseEntity<ReadStatusResponseDTO> modifyReadStatusMessage(
      @RequestBody ReadStatusUpdateDTO updateDTO) {
    ReadStatusResponseDTO update = readStatusService.update(updateDTO);
    return ResponseEntity.ok(update);
  }

  //특정 사용자의 메시지 수신 정보 조회
  @Operation(summary = "특정 사용자의 메세지 수신 정보 조회", description = " 유저 아이디를 통해 특정 사용자의  메세지 수신 정보 조회")
  @GetMapping("/{userId}")
  public ResponseEntity<List<ReadStatusResponseDTO>> getReadStatusUser(@PathVariable UUID userId) {
    List<ReadStatusResponseDTO> statusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statusList);
  }


}
