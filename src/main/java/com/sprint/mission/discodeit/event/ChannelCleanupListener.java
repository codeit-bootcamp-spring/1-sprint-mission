package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelCleanupListener {
    private final ReadStatusService readStatusService;
    private final MessageService messageService;

    @EventListener
    public void handlerChannelDeleted(ChannelDeletedEvent event) {
        Channel channel = event.channel();

        // 해당 채널 readStatus 삭제
        readStatusService.deleteByChannelId(channel.getId());

        // 해당 채널 메시지 삭제
        List<MessageDto> messages = messageService.findAllByChannelId(channel.getId());
        for (MessageDto message : messages) {
            messageService.delete(message.getId());
        }
    }
}
