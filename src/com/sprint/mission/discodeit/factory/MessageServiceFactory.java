package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.MessageService;

public interface MessageServiceFactory {
    MessageService createMessageService();
}
