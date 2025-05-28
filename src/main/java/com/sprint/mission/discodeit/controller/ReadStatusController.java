package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ReadStatusApiDocs;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReadStatusController implements ReadStatusApiDocs {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @PostMapping("/readStatuses")
  public ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @Valid @RequestBody CreateReadStatusDto dto, @AuthenticationPrincipal UserDetails details) {
    ReadStatusResponseDto status = readStatusMapper.toReadStatusResponseDto(
        readStatusService.create(dto, details));
    return ResponseEntity.status(HttpStatus.CREATED).body(status);

  }

  @Override
  @PatchMapping("/readStatuses/{id}")
  public ResponseEntity<ReadStatusResponseDto> updateReadStatus(@PathVariable String id,
      @RequestBody UpdateReadStatusDto dto, @AuthenticationPrincipal UserDetails details) {
    ReadStatus status = readStatusService.updateById(dto, id, details);
    return ResponseEntity.ok(readStatusMapper.toReadStatusResponseDto(status));
  }

  @Override
  @GetMapping("/readStatuses")
  public ResponseEntity<List<ReadStatusResponseDto>> getUserReadStatus(
      @RequestParam String userId) {
    List<ReadStatusResponseDto> status = readStatusService.findAllByUserId(userId).stream()
        .map(readStatusMapper::toReadStatusResponseDto).toList();
    return ResponseEntity.ok(status);
  }
}
