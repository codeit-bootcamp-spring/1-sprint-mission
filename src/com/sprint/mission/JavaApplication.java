package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;

public class JavaApplication {
    private static void testBasicUserService() {
        User frog = new User.Builder("frog", "frog@email.com")
                .build();
        User baek = new User.Builder("baek", "baek@email.com")
                .phoneNumber("010-1234-5678")
                .build();
        User fffrog = new User.Builder("fffrog", "fffrog@email.com")
                .build();
        User ppprog = new User.Builder("ppprog", "")
                .build();
        UUID randomKey = UUID.randomUUID();

        UserRepository userRepository = UserRepositoryFactory.JCF_USER_REPOSITORY_FACTORY.createUserRepository();
        UserService    userService    = UserServiceFactory.JCF_USER_SERVICE_FACTORY.createUserService(userRepository);

        BasicUserService.setupUser(userService, frog);
        BasicUserService.setupUser(userService, baek);
        BasicUserService.setupUser(userService, frog);

        BasicUserService.searchUser(userService, frog.getId());
        BasicUserService.searchUser(userService, baek.getId());
        BasicUserService.searchUser(userService, randomKey);

        BasicUserService.updateUser(userService, frog.getId(), fffrog);
        BasicUserService.updateUser(userService, frog.getId(), ppprog);

        BasicUserService.removeUser(userService, frog.getId());
        BasicUserService.removeUser(userService, frog.getId());
    }

    private static void testBasicMessageService() {
        Message hi  = Message.createMessage("hi");
        Message lo  = Message.createMessage("lo");
        Message mid = Message.createMessage(hi.getId(), "mid");
        Message mmm = Message.createMessage("mmmmmmmmmmmmmmmmmmmmm");
        UUID randomKey = UUID.randomUUID();

        MessageRepository messageRepository = MessageRepositoryFactory.JCF_MESSAGE_REPOSITORY_FACTORY.createMessageRepository();
        MessageService    messageService    = MessageServiceFactory.JCF_MESSAGE_SERVICE_FACTORY.createMessageService(messageRepository);

        BasicMessageService.setupMessage(messageService, hi);
        BasicMessageService.setupMessage(messageService, lo);
        BasicMessageService.setupMessage(messageService, lo);

        BasicMessageService.searchMessage(messageService, hi.getId());
        BasicMessageService.searchMessage(messageService, lo.getId());
        BasicMessageService.searchMessage(messageService, randomKey);

        BasicMessageService.updateMessage(messageService, hi.getId(), mid);
        BasicMessageService.updateMessage(messageService, hi.getId(), mmm);

        BasicMessageService.removeMessage(messageService, hi.getId());
        BasicMessageService.removeMessage(messageService, hi.getId());
    }

    private static void testBasicChannelService() {
        Channel c1 = Channel.createChannel("c1");
        Channel c2 = Channel.createChannel("c2");
        Channel c3 = Channel.createChannel(c1.getId(), "c3");
        Channel cc = Channel.createChannel("c12345678910");
        UUID randomKey = UUID.randomUUID();

        ChannelRepository channelRepository = ChannelRepositoryFactory.JCF_CHANNEL_REPOSITORY_FACTORY.createChannelRepository();
        ChannelService    channelService    = ChannelServiceFactory.JCF_CHANNEL_SERVICE_FACTORY.createChannelService(channelRepository);

        BasicChannelService.setupChannel(channelService, c1);
        BasicChannelService.setupChannel(channelService, c2);
        BasicChannelService.setupChannel(channelService, c1);

        BasicChannelService.searchChannel(channelService, c1.getId());
        BasicChannelService.searchChannel(channelService, c2.getId());
        BasicChannelService.searchChannel(channelService, randomKey);

        BasicChannelService.updateChannel(channelService, c1.getId(), c3);
        BasicChannelService.updateChannel(channelService, c1.getId(), cc);

        BasicChannelService.removeChannel(channelService, c1.getId());
        BasicChannelService.removeChannel(channelService, c1.getId());
    }

    public static void main(String[] args) {
        testBasicUserService();
        testBasicMessageService();
        testBasicChannelService();
    }
}
