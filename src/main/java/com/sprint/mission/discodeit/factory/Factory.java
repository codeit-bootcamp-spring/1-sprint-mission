package com.sprint.mission.discodeit.factory;
import com.sprint.mission.discodeit.observer.Observer;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidtor;
import com.sprint.mission.discodeit.validation.MessageValidator;
import com.sprint.mission.discodeit.validation.UserValidator;

public interface Factory {

    UserService createUserService();

    UserService  createFileUserService();

    ChannelService createChannelService();

    ChannelService createFileChannelService();

    MessageService createMessageService();

    MessageService createFileMessageService();

    UserValidator createUserValidator();

    ChannelValidtor createChannelValidator();

    MessageValidator createMessageValidator();


    UserRepository createUserRepository();

    UserRepository createFileUserRepository();


    ChannelRepository createFileChannelRepository();

    ChannelRepository createChannelRepository();

    MessageRepository createMessageRepository();

    MessageRepository createFileMessageRepository();


    //Observer
    ObserverManager createObserverManager();

    Observer createChannelObserver();
}
