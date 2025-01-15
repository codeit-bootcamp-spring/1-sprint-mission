package com.sprint.mission.discodeit.factory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

public class ServiceFactory implements Factory{
    private static UserService userServiceInstance;
    private static ChannelService channelServiceInstance;
    private static MessageService messageServiceInstance;
    private static MessageValidator messageValidatorInstance;

    @Override
    public synchronized UserService  createUserService() {
        if(userServiceInstance == null){
            userServiceInstance = new JCFUserService();
        }
        return userServiceInstance;
    }

    @Override
    public synchronized ChannelService createChannelService() {
        if(channelServiceInstance == null){
            channelServiceInstance = new JCFChannelService();
        }
        return channelServiceInstance;
    }

    @Override
    public synchronized MessageValidator createMessageValidator(){
        if(messageValidatorInstance == null){
            messageValidatorInstance = new MessageValidator(createChannelService(), createUserService());
        }
        return messageValidatorInstance;
    }

    @Override
    public synchronized MessageService createMessageService() {
        if(messageValidatorInstance == null){
            messageServiceInstance = new JCFMessageService(createMessageValidator());
        }
        return messageServiceInstance;
    }
}