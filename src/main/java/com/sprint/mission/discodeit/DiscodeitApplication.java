package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		UserCreateRequest request = new UserCreateRequest("Abraham", "abraham@naver.com", "abpassword");
		User user = userService.createUser(request, Optional.empty());
		UserCreateRequest request2 = new UserCreateRequest("Brown", "brown@naver.com", "brownpassword");
		User user2 = userService.createUser(request2, Optional.empty());

		PublicChannelCreateRequest request3 = new PublicChannelCreateRequest("공지", "공지 채널입니다.");
		Channel channel = channelService.createChannel(request3);

		MessageCreateRequest request4 = new MessageCreateRequest("안녕하세요.", channel.getId(), user.getId());
		Message message = messageService.createMessage(request4, new ArrayList<>());

		System.out.println("파일 정상종료");








	}

}
