package com.sprint.mission.discodeit.controller.legacy;

import com.sprint.mission.discodeit.entity.status.read.ChannelReadStatus;
import com.sprint.mission.discodeit.entity.status.read.UserReadStatusResponse;
import com.sprint.mission.discodeit.service.status.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class MessageReadStatusController {

  private final ReadStatusService readStatusService;

  @RequestMapping(value = "/{channelId}", method = RequestMethod.POST)
  public ChannelReadStatus createChannelMessageReadStatus(
      @PathVariable("channelId") String channelId) {
    return readStatusService.createChannelReadStatus(channelId);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
  public ChannelReadStatus updateChannelMessageReadStatus(
      @PathVariable("channelId") String channelId) {
    return readStatusService.updateChannelReadStatus(channelId);
  }

  @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
  public Map<UUID, UserReadStatusResponse> readUserMessageStatus(
      @PathVariable("userName") String userName) {
    return readStatusService.findAllByUserId(userName);
  }
}
