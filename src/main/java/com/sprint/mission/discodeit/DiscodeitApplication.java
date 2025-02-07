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


@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService) {
		User user = userService.createUser("홍길동");
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		Channel channel = channelService.createChannel("공지채널");
		return channel;
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = messageService.createMessage(author.getId(), channel.getId(), "안녕하세요");
		System.out.println("메시지 생성: " + message.getId());
	}


	public static void main(String[] args) {
		//Spring의 애플리케이션 컨텍스트 인터페이스로, 빈의 생성과 관계 설정을 담당한다.
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		
		//서비스 초기화, context에서 각 서비스 빈을 가져온다.
		UserService userService=context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		//셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		System.out.println(channel.getChannelName());
		user.getUserName();

		//테스트
		messageCreateTest(messageService, channel, user);




	}

}
