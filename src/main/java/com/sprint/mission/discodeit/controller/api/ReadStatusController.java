package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.ReadStatusApiDocs;
import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-status")
public class ReadStatusController implements ReadStatusApiDocs {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Override
  public ReadStatusResponse createReadStatus(@RequestBody ReadStatusRequest readStatusRequest) {
    return readStatusService.create(readStatusRequest);
  }

  @PutMapping("/{readStatusId}")
  @Override
  public ReadStatusResponse updateReadStatus(@PathVariable UUID readStatusId) {
    return readStatusService.update(readStatusId);
  }

  @GetMapping
  @Override
  public List<ReadStatusResponse> getReadStatusByUser(@RequestParam("userId") UUID userId) {
    return readStatusService.findAllByUserId(userId);
  }
}
