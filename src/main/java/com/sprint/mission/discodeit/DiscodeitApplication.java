package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	// 셋업 및 테스트 메서드 이전
	static User setupUser(UserService userService) {
		User user = userService.create("woody", "woody@codeit.com", "woody1234");
		System.out.println("사용자 생성: " + user.getId() + ", 이름: " + user.getUsername());
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
		System.out.println("채널 생성: " + channel.getId() + ", 이름: " + channel.getName());
		return channel;
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
		System.out.println("메시지 생성: " + message.getId() + ", 내용: " + message.getContent());
	}

	public static void main(String[] args) {
		// Spring Context 초기화
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// Spring Context에서 Service Bean 조회
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// 셋업 및 테스트 실행
		System.out.println("=== 테스트 시작 ===");
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);
		System.out.println("=== 테스트 완료 ===");
	}
}