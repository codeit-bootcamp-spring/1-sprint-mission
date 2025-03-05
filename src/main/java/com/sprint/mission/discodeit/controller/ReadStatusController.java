package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusSwagger;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusListResponse;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

  //implements ReadStatusSwagger
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusResponse> create(
      @RequestBody ReadStatusCreateRequest request
  ) {
    ReadStatus readStatus = readStatusService.create(request);
    ReadStatusResponse response = ReadStatusResponse.from(readStatus);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

//  @PatchMapping(value = "/{readStatusId}")
//  public ResponseEntity<ReadStatusResponse> update(
//      @PathVariable UUID readStatusId,
//      @RequestBody ReadStatusUpdateRequest request
//  ) {
//    ReadStatus readStatus = readStatusService.update(readStatusId, request);
//    return ResponseEntity.ok(ReadStatusResponse.from(readStatus));
//  }


  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusResponse> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request
  ) {
    ReadStatus readStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity.ok(ReadStatusResponse.from(readStatus));
  }

  //  @GetMapping(value = "/users/{userId}")
  public ResponseEntity<ReadStatusListResponse> findV0(
      @PathVariable UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

    return ResponseEntity.ok(ReadStatusListResponse.from(readStatuses));
  }


  @GetMapping
  public ResponseEntity<List<ReadStatusResponse>> findAll(
      @RequestParam UUID userId
  ) {
    List<ReadStatusResponse> readStatuses = readStatusService.findAllByUserId(userId)
        .stream().map(ReadStatusResponse::from).toList();

    return ResponseEntity.ok(readStatuses);
  }
}