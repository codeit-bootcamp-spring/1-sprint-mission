package com.sprint.misson.discordeit.factory.service;

import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;
import com.sprint.misson.discordeit.service.file.FileChannelService;
import com.sprint.misson.discordeit.service.file.FileMessageService;
import com.sprint.misson.discordeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory {

    private static FileServiceFactory instance;

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    private FileServiceFactory() {
        this.userService = new FileUserService();
        this.channelService = new FileChannelService(userService);
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
    public ChannelService getChannelService() {
        return channelService;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }
}
