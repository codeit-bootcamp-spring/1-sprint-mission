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
		CreateUserDTO createUserDTO = CreateUserDTO.builder()
				.username("woody")
				.email("woody@codeit.com")
				.password("woody1234")
				.build();
		return userService.create(createUserDTO);
	}

	static Channel setupChannel(ChannelService channelService) {
		ChannelDTO.PublicChannelDTO publicChannelDTO = new ChannelDTO.PublicChannelDTO("공지", "공지 채널입니다.");
		return channelService.createPublicChannel(publicChannelDTO);
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		MessageDTO.CreateMessageDTO createMessageDTO = MessageDTO.CreateMessageDTO.builder()
				.content("안녕하세요.")
				.channelId(channel.getId())
				.authorId(author.getId())
				.build();
		Message message = messageService.create(createMessageDTO);
		System.out.println("메시지 생성: " + message.getId());
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		//서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);
	}

}
