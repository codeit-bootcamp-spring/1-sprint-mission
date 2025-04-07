package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.ChannelApiDocs;
import com.sprint.mission.discodeit.dto.request.ChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApiDocs {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> createPublicChannel(
      @Valid @RequestBody ChannelRequest.CreatePublic publicChannelRequest) {

    log.info("POST /api/channels/public - channel: {}", publicChannelRequest.getName());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(channelService.createPublicChannel(publicChannelRequest)));
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> createPrivateChannel(
      @Valid @RequestBody ChannelRequest.CreatePrivate privateChannelRequest) {

    log.info("POST /api/channels/private");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            CustomApiResponse.created(channelService.createPrivateChannel(privateChannelRequest)));
  }

  @PutMapping("/public/{channelId}")
  @Override
  public ResponseEntity<CustomApiResponse<ChannelResponse>> updatePublicChannel(
      @PathVariable UUID channelId,
      @Valid @RequestBody ChannelRequest.Update publicChannelRequest
  ) {

    log.info("PUT /api/channels/public/{}", channelId);
    return ResponseEntity.ok(
        CustomApiResponse.success(channelService.update(channelId, publicChannelRequest))
    );
  }

  @DeleteMapping("/{channelId}")
  @Override
  public ResponseEntity<CustomApiResponse<Void>> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteById(channelId);
    log.info("DELETE /api/channels/{}", channelId);
    return ResponseEntity.ok(CustomApiResponse.success("Channel deleted successfully"));
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<ChannelResponse>>> getChannelListByUser(
      @RequestParam("userId") UUID userId) {
    return ResponseEntity.ok(CustomApiResponse.success(channelService.findAllByUserId(userId)));
  }
}
