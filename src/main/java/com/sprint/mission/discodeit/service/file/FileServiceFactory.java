package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

public class FileServiceFactory implements ServiceFactory {
    private static final UserService userService = FileUserService.getInstance();
    private static final ChannelService channelService = FileChannelService.getInstance();
    private static final MessageService messageService = FileMessageService.getInstance(userService, channelService);

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}