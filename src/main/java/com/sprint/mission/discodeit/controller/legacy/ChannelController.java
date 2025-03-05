package com.sprint.mission.discodeit.controller.legacy;


import com.sprint.mission.discodeit.controller.Result;
import com.sprint.mission.discodeit.entity.channel.ChannelListResponse;
import com.sprint.mission.discodeit.entity.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.add.ChannelAddUserResponse;
import com.sprint.mission.discodeit.entity.channel.create.ChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelUpdateResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  /**
   * 기존의 채널 생성 endpoint
   */
  @RequestMapping(method = RequestMethod.POST)
  public ChannelResponse createChannel(@Validated @RequestBody ChannelCreateRequest request) {
    return channelService.createChannel(request);
  }

  @RequestMapping(value = "/{channelName}", method = RequestMethod.PUT)
  public ChannelUpdateResponse updateChannel(@PathVariable("channelName") String channelName,
      @Validated @RequestBody ChannelUpdateRequest request) {

    return channelService.updateChannel(channelName, request);
  }

  @RequestMapping(value = "/{channelName}", method = RequestMethod.DELETE)
  public Result<UUID> deleteChannel(@PathVariable("channelName") String channelName) {
    return new Result<>(channelService.removeChannelByName(channelName));
  }

  /**
   * 유저가 가입한 모들 채널 뿌리기
   */
  @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
  public Map<UUID, ChannelListResponse> getAllChannelOfUser(
      @PathVariable("userName") String userName) {
    return channelService.getAllChannelsOfUser(userName);
  }

  /**
   * 채널에 유저 초대
   */
  @RequestMapping(value = "/{channelId}/{userName}", method = RequestMethod.POST)
  public Result<ChannelAddUserResponse> addUser(@PathVariable("channelId") UUID channelId,
      @PathVariable("userName") String userName) {
    ChannelAddUserResponse channelAddUserResponse = channelService.addUserChannel(channelId,
        userName);

    return new Result<>(channelAddUserResponse);
  }
}
