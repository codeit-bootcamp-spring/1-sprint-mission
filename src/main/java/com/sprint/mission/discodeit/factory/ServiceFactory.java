package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileNotFoundException;

public interface ServiceFactory {
    UserService createUserService() throws FileNotFoundException;
    ChannelService createChannelService() throws FileNotFoundException;
    MessageService createMessageService() throws FileNotFoundException;
}
