package com.sprint.mission.discodeit.observer;

import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class ChannelObserver implements Observer{
    private final MessageService messageService;

    public ChannelObserver(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void update(UUID uuid) {
        messageService.deleteAllMessagesForChannel(uuid);
    }
}
