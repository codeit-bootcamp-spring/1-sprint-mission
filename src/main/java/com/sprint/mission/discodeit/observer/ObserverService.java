package com.sprint.mission.discodeit.observer;

import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class ObserverService implements Observer{
    private final MessageService messageService;

    public ObserverService(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public void update(UUID channeluuId) {
        messageService.deleteMessagesByChannel(channeluuId);
    }
}
