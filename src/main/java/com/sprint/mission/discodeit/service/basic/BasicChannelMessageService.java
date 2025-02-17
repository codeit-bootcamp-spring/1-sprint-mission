package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.messageDto.FindMessageResponseDto;
import com.sprint.mission.discodeit.service.ChannelMessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelMessageService implements ChannelMessageService {
    private final ChannelService channelService;
    private final MessageService messageService;

    @Override
    public void getLastMessageTime(UUID channelId) {

        // 해당 채널 메시지 가져옴
        List<FindMessageResponseDto> messages = messageService.findAllByChannelId(channelId);

        // 가장 최근 생성된 메시지 시간을 채널에 세팅
        if (!messages.isEmpty()) {
            channelService.updateLastMessageTime(channelId, messages.get(messages.size() - 1).getCreateAt());
        }
    }
}
