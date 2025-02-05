package com.sprint.mission.discodeit;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

@SpringBootApplication
public class DiscodeitApplication {
	static User setupUser(UserService userService) {
		return userService.createUser(new User("user01", "woody", "woody@codeit.com", "woody1234"));
	}

	static Channel setupChannel(ChannelService channelService) {
		return channelService.createChannel(
			new Channel("General", "공지", new HashMap<>(), new ArrayList<>(), ChannelType.GROUP));
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = messageService.createMessage(new Message("안녕하세요.", author, channel));
		System.out.println("메시지 생성: " + message.getId());
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ct = SpringApplication.run(DiscodeitApplication.class, args);

		//Spring DI로 인하여 factory 일시적으로 제거
		ChannelService channelService = ct.getBean(ChannelService.class);
		UserService userService = ct.getBean(UserService.class);
		MessageService messageService = ct.getBean(MessageService.class);

		System.out.println("=== 서비스 테스트 시작 ===");
		try {
			// 셋업
			User user = setupUser(userService);
			System.out.println("생성된 사용자: " + user);

			Channel channel = setupChannel(channelService);
			channelService.addParticipantToChannel(channel.getId(), user.getId());
			System.out.println("생성된 채널: " + channel);

			// 테스트
			messageCreateTest(messageService, channel, user);

			System.out.println("=== 서비스 테스트 완료 ===");
		} catch (Exception e) {
			System.err.println("테스트 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
}