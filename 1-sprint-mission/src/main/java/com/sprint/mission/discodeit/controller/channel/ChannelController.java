package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    //공개 채널 생성
    @PostMapping("/public")
    public ChannelResponseDTO createChannel(@RequestBody PublicChannelCreateDTO createDTO) {
        return channelService.createPublicChannel(createDTO);
    }

    //비공개 채널 생성
    @PostMapping("/private")
    public ChannelResponseDTO createPrivateChannel(@RequestBody PrivateChannelCreateDTO createDTO) {
        return channelService.createPrivateChannel(createDTO);
    }

    //공개 채널의 정보를 수정
    @PatchMapping("/public")
    public ChannelResponseDTO updatePublicChannel(@RequestBody ChannelUpdateDTO updateDTO) {
        return channelService.update(updateDTO);
    }

    //채널 삭제
    @DeleteMapping("/{channelId}")
    public void deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
    }

    //특정 사용자가 볼 수 있는 모든 채널 목록을 조회
    @GetMapping("/list/{userId}")
    public List<ChannelResponseDTO> getUserAllChannels(@PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }


}
