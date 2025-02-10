package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.ChannelServiceFactory;
import com.sprint.mission.discodeit.factory.MessageServiceFactory;
import com.sprint.mission.discodeit.factory.UserServiceFactory;
import com.sprint.mission.discodeit.factory.type.repository.ChannelRepositoryType;
import com.sprint.mission.discodeit.factory.type.repository.MessageRepositoryType;
import com.sprint.mission.discodeit.factory.type.repository.UserRepositoryType;
import com.sprint.mission.discodeit.factory.type.service.ChannelServiceType;
import com.sprint.mission.discodeit.factory.type.service.MessageServiceType;
import com.sprint.mission.discodeit.factory.type.service.UserServiceType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {

	private static void testBasicUserService(UserService userService) {
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

	private static void testBasicChannelService(ChannelService channelService) {
		Channel c1 = Channel.createChannel("c1");
		Channel c2 = Channel.createChannel("c2");
		Channel c3 = Channel.createChannel(c1.getId(), "c3");
		Channel cc = Channel.createChannel("c12345678910");
		UUID randomKey = UUID.randomUUID();

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

	private static void testBasicMessageService(MessageService messageService) {
		Message hi  = Message.createMessage("hi");
		Message lo  = Message.createMessage("lo");
		Message mid = Message.createMessage(hi.getId(), "mid");
		Message mmm = Message.createMessage("mmmmmmmmmmmmmmmmmmmmm");
		UUID randomKey = UUID.randomUUID();

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

	private static void testBasicUserService() {
		UserService userService = UserServiceFactory.createService(UserServiceType.JCF, UserRepositoryType.JCF);
		//testBasicUserService(userService);

		userService = UserServiceFactory.createService(UserServiceType.FILE, UserRepositoryType.FILE);
		testBasicUserService(userService);
	}

	private static void testBasicChannelService() {
		ChannelService channelService = ChannelServiceFactory.createService(ChannelServiceType.JCF, ChannelRepositoryType.JCF);
		//testBasicChannelService(channelService);

		channelService = ChannelServiceFactory.createService(ChannelServiceType.FILE, ChannelRepositoryType.FILE);
		testBasicChannelService(channelService);
	}

	private static void testBasicMessageService() {
		MessageService messageService = MessageServiceFactory.createService(MessageServiceType.JCF, MessageRepositoryType.JCF);
		//testBasicMessageService(messageService);

		messageService = MessageServiceFactory.createService(MessageServiceType.FILE, MessageRepositoryType.FILE);
		testBasicMessageService(messageService);
	}

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}

}
