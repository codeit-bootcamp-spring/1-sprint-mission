package com.sprint.mission.discodeit.factory.file;

import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;


public class FileServiceFactory implements ServiceFactory {

    //서비스 객체 생성
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public FileServiceFactory() {
        this.userService = createUserService();
        this.channelService = createChannelService();
        this.messageService = createMessageService();
    }

    @Override
    public UserService createUserService() {
        return new FileUserService(messageService,channelService);
    }

    @Override
    public ChannelService createChannelService() {
        return new FileChannelService(userService,messageService);
    }

    @Override
    public MessageService createMessageService() {
        return new FileMessageService(userService,channelService);
    }
}
