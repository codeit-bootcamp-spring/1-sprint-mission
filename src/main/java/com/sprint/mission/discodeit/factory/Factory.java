package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public interface Factory {
    UserService getUserService();
    MessageService getMessageService();
    ChannelService getChannelService();
}
