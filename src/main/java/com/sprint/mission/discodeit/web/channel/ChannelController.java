package com.sprint.mission.discodeit.web.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelGroup;
import com.sprint.mission.discodeit.entity.Participant;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.web.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.web.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.web.dto.PublicChannelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<List<PublicChannelDto>> createPublicChannel() {
        User hyun = new User("hyun", "hyun123", "Hyun", "hyun@example.code");
        User yull = new User("yull", "yull123", "Yull", "yull@example.code");
        Channel hiChannel = new Channel("hiChannel", "Hi", ChannelGroup.PUBLIC);
        Channel byeChannel = new Channel("byeChannel", "Bye", ChannelGroup.PUBLIC);

        PublicChannelDto publicChannelHi = new PublicChannelDto(hiChannel);
        PublicChannelDto publicChannelBye = new PublicChannelDto(byeChannel);

        Participant hiParticipation = new Participant(yull);
        Participant hiParticipation2 = new Participant(hyun);
        Participant byeParticipation = new Participant(hyun);

        channelService.createPublicChannel(publicChannelHi, hiParticipation, hiParticipation2);
        channelService.createPublicChannel(publicChannelBye, byeParticipation);
        return ResponseEntity.ok(channelService.findAllPublicChannels());
    }

    @PostMapping("/private")
    public ResponseEntity<List<PrivateChannelDto>> createPrivateChannel() {
        User hyun = new User("hyun", "hyun123", "Hyun", "hyun@example.code");
        Channel howChannel = new Channel("How", "How about u?", ChannelGroup.PRIVATE);
        PrivateChannelDto privateChannel = new PrivateChannelDto(howChannel);
        Participant howParticipation = new Participant(hyun);
        channelService.createPrivateChannel(privateChannel, howParticipation);
        return ResponseEntity.ok(channelService.findAllPrivateChannels());
    }

    @PutMapping("{id}")
    public ResponseEntity<List<PublicChannelDto>> updateChannel(@PathVariable UUID id, @RequestBody ChannelUpdateDto channelParam) {
        channelService.updateChannel(id, channelParam);
        List<PublicChannelDto> allPublicChannels = channelService.findAllPublicChannels();
        return ResponseEntity.ok(allPublicChannels);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{loginId}")
    public ResponseEntity<List<UUID>> findChannelByUserId(@PathVariable String loginId) {
        User hyun = new User("hyun", "hyun123", "Hyun", "hyun@example.code");
        Channel hiChannel = new Channel("hiChannel", "Hi", ChannelGroup.PUBLIC);
        PublicChannelDto publicChannelHi = new PublicChannelDto(hiChannel);
        Participant hiParticipation2 = new Participant(hyun);
        channelService.createPublicChannel(publicChannelHi, hiParticipation2);
        return ResponseEntity.ok(channelService.findAllByLoginId(loginId));
    }
}
