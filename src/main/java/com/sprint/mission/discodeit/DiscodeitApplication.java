package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.observer.service.ChannelObserver;
import com.sprint.mission.discodeit.observer.Observer;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService){
		User user = userService.createUser
				("코드일","codeit1234@codeit.com","codeit1234","1Q2w3e4r@");
		return user;
	}

	static User setupUser2(UserService userService){
		User user = userService.createUser
				("코드이","code1234@codeit.com","codeit1234","1q2W3e4r@");
		return user;
	}


	static Channel setupChannel(ChannelService channelService){
		Channel ch = channelService.createChannel(ChannelType.PUBLIC,"CH.1","테스트 채널 1");
		return ch;
	}

	static Channel setupChannel2(ChannelService channelService){
		Channel ch = channelService.createChannel(ChannelType.PUBLIC,"CH.2", "테스트 채널 2");
		return ch;
	}

	static void allPrintUser(Map<UUID, User> data){
		for(User user  : data.values()){
			System.out.println(user);
			System.out.println("-----------------------------------------------");
		}
	}
	static void allprintChannel(Map<UUID, Channel> data){
		for(Channel channel : data.values()){
			System.out.println(channel);
			System.out.println("-----------------------------------------------");
		}
	}

	static void allprintMessages( Map<UUID, Map<UUID, Message>> data){
		for (Map<UUID, Message> channelMessages : data.values()) {
			for (Message message : channelMessages.values()) {
				System.out.println(message);
				System.out.println("===============================================");
			}
		}
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author,String str){
		Message message = messageService.createMsg(author,channel,str);
		System.out.println("메시지 생성: " + message.getUuId());
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(BasicUserService.class);
		ChannelService channelService = context.getBean(BasicChannelService.class);
		MessageService messageService = context.getBean(BasicMessageService.class);



		User user1 = setupUser(userService);
		User user2 = setupUser2(userService);
		allPrintUser(userService.getAllUsers());

		Channel ch1 = setupChannel(channelService);
		Channel ch2 = setupChannel2(channelService);
		allprintChannel(channelService.getAllChannels());

		messageCreateTest(messageService,ch1,user1,"user1이 ch1으로 보내는 메시지");
		messageCreateTest(messageService,ch1,user2,"user2이 ch1으로 보내는 메시지");
		messageCreateTest(messageService,ch2,user2,"user1이 ch2으로 보내는 메시지");
		messageCreateTest(messageService,ch2,user2,"user2이 ch2으로 보내는 메시지");
		allprintMessages(messageService.getAllMsg());


		System.out.println("----------ch1 채널 삭제후 모든 채널 조회-----------");
		channelService.deleteChannel(ch1.getId());
		System.out.println("----------ch1 채널 삭제후 메시지 조회----------");
		allprintChannel(channelService.getAllChannels());
		allprintMessages(messageService.getAllMsg());
	}
}
