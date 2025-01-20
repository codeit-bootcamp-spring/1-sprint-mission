package com.sprint.misson.discordeit.factory;

import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;
import com.sprint.misson.discordeit.service.file.FileChannelService;
import com.sprint.misson.discordeit.service.file.FileMessageService;
import com.sprint.misson.discordeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory {

    private static FileServiceFactory instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;

    private FileServiceFactory() {
        this.userService = createUserService();
        this.channelService = createChannelService();
        this.messageService = createMessageService();
    }

    public static FileServiceFactory getInstance() {
        if (instance == null) {
            instance = new FileServiceFactory();
        }
        return instance;
    }

    @Override
    public UserService createUserService() {
        if (userService == null) {
            userService = new FileUserService();
        }
        return userService;
    }

    @Override
    public ChannelService createChannelService() {
        if (channelService == null) {
            channelService = new FileChannelService(userService);
        }
        return channelService;
    }

    @Override
    public MessageService createMessageService() {
        if (messageService == null) {
            messageService = new FileMessageService(userService, channelService);
        }
        return messageService;
    }
}
