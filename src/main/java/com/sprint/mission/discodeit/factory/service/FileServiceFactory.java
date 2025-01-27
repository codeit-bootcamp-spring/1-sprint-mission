package com.sprint.mission.discodeit.factory.service;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory {
    private static FileServiceFactory instance;
    private final UserService userService;
    private final MessageService messageService;
    private final ChannelService channelService;

    private FileServiceFactory() {
        this.userService = new FileUserService();
        this.channelService = new FileChannelService();
        this.messageService = new FileMessageService(userService, channelService);
    }

    public static FileServiceFactory getInstance() {
        if (instance == null) {
            instance = new FileServiceFactory();
        }
        return instance;
    }
    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }

    @Override
    public ChannelService getChannelService() {
        return channelService;
    }
}