package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @RequestMapping(method = RequestMethod.GET, value = "/{channelId}")
  public ResponseEntity<List<ReadStatusDto>> getReadStatus(
      @PathVariable("channelId") UUID channelId, @RequestParam("userId") String userId) {
    List<ReadStatusDto> readStatusDtos = readStatusService.findAllByUserId(UUID.fromString(userId));
    return ResponseEntity.ok(readStatusDtos);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/{channelId}")
  public ResponseEntity<ReadStatusDto> createReadStatus(@PathVariable("channelId") UUID channelId,
      @RequestParam("userId") String userId) {
    ReadStatusDto readStatusDto = new ReadStatusDto(UUID.fromString(userId), channelId,
        System.currentTimeMillis());
    readStatusService.create(readStatusDto);
    return ResponseEntity.ok(readStatusDto);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{channelId}")
  public ResponseEntity<Void> updateReadStatus(@PathVariable("channelId") UUID channelId,
      @RequestParam("userId") String userId) {
    readStatusService.updateReadStatus(UUID.fromString(userId), channelId);
    return ResponseEntity.ok().build();
  }
}
