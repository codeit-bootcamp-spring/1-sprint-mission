package com.sprint.mission.discodeit.factory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

public class ServiceFactory implements Factory{

    @Override
    public UserService createUserService() {
        return new JCFUserService();
    }

    @Override
    public ChannelService createChannelService() {
        return new JCFChannelService();
    }

    @Override
    public MessageValidator createMessageValidator(){
        return new MessageValidator(createChannelService(),createUserService());
    }

    @Override
    public MessageService createMessageService() {
        return new JCFMessageService(createMessageValidator());
    }
}