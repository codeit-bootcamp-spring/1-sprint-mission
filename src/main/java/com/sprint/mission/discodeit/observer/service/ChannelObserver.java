package com.sprint.mission.discodeit.observer.service;

import com.sprint.mission.discodeit.observer.Observer;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class ChannelObserver implements Observer {
    private final MessageService messageService;

    public ChannelObserver(MessageService messageService){
        this.messageService = messageService;
    }
    @Override
    public void update(UUID id) {
        messageService.deleteAllMessagesForChannel(id);
    }
}
