package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class BasicFactory implements ServiceFactory {
    static BasicUserService basicUserService = null;
    static BasicChannelService basicChannelService = null;
    static BasicMessageService basicMessageService = null;

    public BasicFactory() {
        super();
        basicUserService = BasicUserService.getInstance(new FileUserRepository(), null, null); //여기는 구현체 만들고 넣기
        basicChannelService = BasicChannelService.getInstance(new FileChannelRepository());
        basicMessageService = BasicMessageService.getInstance(new FileMessageRepository());
    }
    @Override
    public UserService getUserService() {
        return basicUserService;
    }

    @Override
    public ChannelService getChannelService() {
        return basicChannelService;
    }

    @Override
    public MessageService getMessageService() {
        return basicMessageService;
    }
}
