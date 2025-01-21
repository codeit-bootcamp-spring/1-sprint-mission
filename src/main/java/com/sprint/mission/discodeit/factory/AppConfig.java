package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class AppConfig {

    public ChannelService basicChannelService() {
        return new BasicChannelService(new JCFChannelRepository());
        //return new BasicChannelService(new FileChannelRepository());
    }

    public UserService basicUserService() {
        return new BasicUserService(new JCFUserRepository());
    }

    public MessageService basicMessageService() {
        return new BasicMessageService(new JCFMessageRepository());
    }
}
