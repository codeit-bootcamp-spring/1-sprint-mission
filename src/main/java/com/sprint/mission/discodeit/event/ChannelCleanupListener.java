package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.messageDto.FindMessageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelCleanupListener {
    private final ReadStatusService readStatusService;
    private final MessageService messageService;

    @EventListener
    public void handlerChannelDeleted(ChannelDeletedEvent event) {
        Channel channel = event.channel();

        // 채널 readStatus 삭제
        if (channel.getReadStatuses() != null) {
            for (UUID id : channel.getReadStatuses().keySet()) {
                readStatusService.delete(id);
            }
        }

        // 해당 채널 메시지 삭제
        List<FindMessageResponseDto> messages = messageService.findAllByChannelId(channel.getId());
        for (FindMessageResponseDto message : messages) {
            messageService.delete(message.getId());
        }
    }
}
