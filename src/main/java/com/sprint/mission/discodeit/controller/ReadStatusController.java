package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody CreateReadStatusRequest createReadStatusRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.createReadStatus(createReadStatusRequest));
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID id,
      @RequestBody UpdateReadStatusRequest updateReadStatusRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.updateReadStatus(id, updateReadStatusRequest));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusDto>> findAllReadStatusByUserId(
      @RequestParam("userId") UUID id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.findAllReadStatusesByUserId(id));
  }
}
