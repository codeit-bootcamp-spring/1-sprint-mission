package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class ReadStatusController implements ReadStatusApi {

  private final ObjectMapper objectMapper;
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(@RequestBody String jsonRequest)
      throws JsonProcessingException {
    ReadStatusCreateRequest request = objectMapper.readValue(jsonRequest,
        ReadStatusCreateRequest.class);
    ReadStatusDto createdReadStatus = readStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }


  @PatchMapping("/{id}")
  public ResponseEntity<ReadStatusDto> update(
      @Parameter(description = "수정할 읽음 상태 ID", required = true)
      @PathVariable("id") UUID id,
      @RequestBody String jsonRequest) throws JsonProcessingException {
    ReadStatusUpdateRequest request = objectMapper.readValue(jsonRequest,
        ReadStatusUpdateRequest.class);
    ReadStatusDto updatedReadStatus = readStatusService.update(id, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }
}
