package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class FileServiceFactory implements ServiceFactory {
    static FileUserService fileUserService = null;
    static FileChannelService fileChannelService = null;
    static FileMessageService fileMessageService = null;

    public FileServiceFactory() {
        super();
        fileUserService = FileUserService.getInstance();
        fileChannelService = FileChannelService.getInstance();
        fileMessageService = FileMessageService.getInstance();
        fileUserService.setFileMessageService(fileMessageService);
        fileChannelService.setFileMessageService(fileMessageService);
        fileMessageService.setFileChannelService(fileChannelService);
    }

    @Override
    public UserService getUserService() {
        return fileUserService;
    }

    @Override
    public ChannelService getChannelService() {
        return fileChannelService;
    }

    @Override
    public MessageService getMessageService() {
        return fileMessageService;
    }
}

