package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusSwagger;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses") //read-statuses
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusSwagger {

  private final ReadStatusService readStatusService;

  private final ReadStatusMapper readStatusMapper;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @RequestBody ReadStatusCreateRequest request
  ) {
    ReadStatus readStatus = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusMapper.toDto(readStatus));
  }

  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request
  ) {
    ReadStatus readStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity.ok(readStatusMapper.toDto(readStatus));
  }


  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAll(
      @RequestParam UUID userId
  ) {
    List<ReadStatusDto> readStatusesDtos = readStatusMapper.toDtoList(
        readStatusService.findAllByUserId(userId));
    return ResponseEntity.ok(readStatusesDtos);
  }
}