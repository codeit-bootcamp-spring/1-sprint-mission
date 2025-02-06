package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

//		User user = setupUser(userService);
//		Channel channel = setupChannel(user, channelService);
//		messageCreatTest(user, channel, messageService);
		System.out.println(" <<<< 유저 생성 >>>> ");

		User user1 = userService.create(, "강병훈", , "123123132@");
		User user2 = userService.create(, "John", , "qddjddk@");
		User user3 = userService.create(, "Bob", , "a123213kd");
		System.out.println();
		List<User> users = userService.findAll();
		for (User u : users) {
			System.out.println(u.getEmail());
		}

		//사용자 업데이트
		userService.update(, user1.getId(), , "kbh");
		//사용자 삭제
		userService.delete(user3.getId());
		System.out.println();
		// 사용자 출력
		System.out.println(" <<<< All Users >>>>");
		List<User> updateUsers = userService.findAll();
		for (User u : updateUsers) {
			System.out.println(u.getEmail());
		}
		System.out.println();

		//채널 생성
		System.out.println(" <<<< 채널 생성 >>>> ");
		Channel channel1 = channelService.create(user1, "공지", "공지 입니다");
		Channel channel2 = channelService.create(user2, "공지1", "공지 입니다1");

		// 채널 조회
		System.out.println(" <<<< All Channels >>>>");
		List<Channel>channels  = channelService.findAll();
		for (Channel channel : channels) {
			System.out.println(channel.getName());
		}
		//채널 업데이트
		channelService.update(channel1, "공지 new", "공지입니다 !");
		//채널 삭제
		channelService.delete(channel1);
		System.out.println(" <<<< All Channels >>>>");
		//수정된 채널 조회
		List<Channel> channelNew = channelService.findAll();
		for (Channel channel : channelNew) {
			System.out.println(channel.getName());
		}

		System.out.println(" <<<< 메세지 생성 >>>> ");
		//메세지 생성
		Message messageUser1 = messageService.create(user1, channel2, "Hello World");
		Message messageUser2 = messageService.create(user1, channel2, "Hi World");
		Message messageUser3 = messageService.create(user1, channel2, "1231451");
		//메세지 조회
		System.out.println(" <<<< All Messages >>>>");
		List<Message> messages = messageService.findAll();
		for (Message message : messages) {
			System.out.println(message.getContent());
		}

		messageService.delete(user1,channel2, messageUser1);
		messageService.update(user1,channel2,"Error message");

		System.out.println(" <<<< All Messages >>>>");
		List<Message> newMessage = messageService.findAll();
		for (Message message : newMessage) {
			System.out.println(message.getContent());
		}
	}

	private static User setupUser(UserService userService) {
		User user = userService.create(, "Bob", , "1234@xxx.xxx");
		return user;
	}

	private static Channel setupChannel(User user, ChannelService channelService) {
		Channel channel = channelService.create(user, "공지", "공지 채널 생성");
		return channel;
	}

	private static void messageCreatTest(User user, Channel channel, MessageService messageService) {
		Message message1 = messageService.create(user, channel, "Hello World!");
		Message message2 = messageService.create(user, channel, "Hi World!");
	}

}
