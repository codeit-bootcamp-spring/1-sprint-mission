package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.ChannelApiDocs;
import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApiDocs {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> createPublicChannel(
      @RequestBody ChannelRequest.CreatePublic publicChannelRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(channelService.createPublicChannel(publicChannelRequest)));
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> createPrivateChannel(
      @RequestBody ChannelRequest.CreatePrivate privateChannelRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            CustomApiResponse.created(channelService.createPrivateChannel(privateChannelRequest)));
  }

  @PutMapping("/public/{channelId}")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> updatePublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelRequest.Update publicChannelRequest
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(channelService.update(channelId, publicChannelRequest))
    );
  }

  @DeleteMapping("/{channelId}")
  @Override
  public ResponseEntity<CustomApiResponse<Void>> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteById(channelId);
    return ResponseEntity.ok(CustomApiResponse.success("Channel deleted successfully"));
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<ChannelResponse>>> getChannelListByUser(
      @RequestParam("userId") UUID userId) {
    return ResponseEntity.ok(CustomApiResponse.success(channelService.findAllByUserId(userId)));
  }
}
