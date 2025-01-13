package com.sprint.mission.discodeit.factory.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFServiceFactory implements ServiceFactory {
    private final Map<UUID, User> userData;
    private final Map<UUID, Channel> channelData;
    private final Map<UUID, Message> messageData;

    public JCFServiceFactory() {
        this.userData = new HashMap<>();
        this.channelData = new HashMap<>();
        this.messageData = new HashMap<>();
    }

    @Override
    public UserService createUserService(){
        JCFUserService userService = new JCFUserService(userData);
        ChannelService channelService = createChannelService();
        MessageService messageService = createMessageService();
        userService.setDependencies(messageService, channelService);
        return userService;
    }

    @Override
    public ChannelService createChannelService() {
        JCFChannelService channelService = new JCFChannelService(channelData);
        UserService userService = createUserService();
        MessageService messageService = createMessageService();
        channelService.setDependencies(userService, messageService);
        return channelService;
    }

    @Override
    public MessageService createMessageService() {
        JCFMessageService messageService = new JCFMessageService(messageData);
        UserService userService = createUserService();
        ChannelService channelService = createChannelService();
        messageService.setDependencies(userService, channelService);
        return messageService;
    }
}
