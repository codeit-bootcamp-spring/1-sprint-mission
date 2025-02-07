package com.sprint.mission.discodeit.factory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import com.sprint.mission.discodeit.validation.MessageValidator;
import com.sprint.mission.discodeit.validation.UserValidator;

public interface Factory {
    //UserService
    UserService createJCFUserService();

    UserService createFileUserService();

    UserService createBasicUserService();
    //ChannelService
    ChannelService createJCFChannelService();

    ChannelService createFileChannelService();

    ChannelService createBasicChannelService();

    //MessageService
    MessageService createJCFMessageService();

    MessageService createFileMessageService();

    MessageService createBasicMessageService();
    //Validator
    UserValidator createUserValidator();

    ChannelValidator createChannelValidator();

    MessageValidator createMessageValidator();

    //repository(User)
    UserRepository createJCFUserRepository();

    UserRepository createFileUserRepository();

    //repository(Channel)
    ChannelRepository createFileChannelRepository();

    ChannelRepository createJCFChannelRepository();

    //repository(Message)
    MessageRepository createJCFMessageRepository();

    MessageRepository createFileMessageRepository();
}
