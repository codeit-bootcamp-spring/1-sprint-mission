package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.MessageService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.config.ChannelFactory;
import com.sprint.mission.discodeit.config.MessageFactory;
import com.sprint.mission.discodeit.config.UserFactory;

public class JavaApplication {

    public static void main(String[] args) {
        UserService userService = UserFactory.getUserService();
        ChannelService channelService = ChannelFactory.getChannelService();
        MessageService messageService = MessageFactory.getMessageService();
    }
}
