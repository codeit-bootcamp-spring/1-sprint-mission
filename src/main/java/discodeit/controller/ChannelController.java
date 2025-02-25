package discodeit.controller;

import discodeit.dto.channel.*;
import discodeit.entity.Channel;
import discodeit.entity.ChannelType;
import discodeit.entity.Message;
import discodeit.repository.MessageRepository;
import discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<PublicChannelDto> createPublicChannel(@RequestBody CreatePublicChannelRequest request) {
        Channel publicChannel = channelService.create(request);
        Instant lastMessageAt = getLastMessageAt(publicChannel.getId());
        return ResponseEntity.ok(PublicChannelDto.fromDomain(publicChannel, lastMessageAt));
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<PrivateChannelDto> createPrivateChannel(@RequestBody CreatePrivateChannelRequest request) {
        Channel privateChannel = channelService.create(request);
        Instant lastMessageAt = getLastMessageAt(privateChannel.getId());
        return ResponseEntity.ok(PrivateChannelDto.fromDomain(privateChannel, lastMessageAt));
    }

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public ResponseEntity<PublicChannelDto> getPublicChannel(@RequestParam("id") UUID channelId) {
        Channel publicChannel = channelService.find(channelId);
        Instant lastMessageAt = getLastMessageAt(publicChannel.getId());
        return ResponseEntity.ok(PublicChannelDto.fromDomain(publicChannel, lastMessageAt));
    }

    @RequestMapping(value = "/private", method = RequestMethod.GET)
    public ResponseEntity<PrivateChannelDto> getPrivateChannel(@RequestParam("id") UUID channelId) {
        Channel privateChannel = channelService.find(channelId);
        Instant lastMessageAt = getLastMessageAt(privateChannel.getId());
        return ResponseEntity.ok(PrivateChannelDto.fromDomain(privateChannel, lastMessageAt));
    }

    @RequestMapping(value = "/{userId}/channel-list", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getAllChannelByUserId(@PathVariable("userId") UUID userId) {
        List<Channel> channelList = channelService.findAllByUserId(userId);

        List<Object> channelDtos = channelList.stream()
                .map(channel -> {
                    Instant lastMessageAt = getLastMessageAt(channel.getId());
                    return channel.getType().equals(ChannelType.PRIVATE)
                            ? PrivateChannelDto.fromDomain(channel, lastMessageAt)
                            : PublicChannelDto.fromDomain(channel, lastMessageAt);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(channelDtos);
    }

    @RequestMapping(value = "/public", method = RequestMethod.PATCH)
    public ResponseEntity<PublicChannelDto> updateChannel(@RequestBody UpdateChannelRequest request) {
        Channel channel = channelService.update(request);
        Instant lastMessageAt = getLastMessageAt(request.id());
        return ResponseEntity.ok(PublicChannelDto.fromDomain(channel, lastMessageAt));
    }

    @RequestMapping(value = "/private", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePrivateChannel(@RequestParam("id") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("채널 ID: " + channelId + " delete Complete!!");
    }

    private Instant getLastMessageAt(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId)
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);
    }

}
