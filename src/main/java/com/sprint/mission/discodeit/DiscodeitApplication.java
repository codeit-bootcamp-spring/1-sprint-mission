package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ct = SpringApplication.run(DiscodeitApplication.class, args);

		//Spring DI로 인하여 factory 일시적으로 제거
		ChannelService channelService = ct.getBean(ChannelService.class);
		UserService userService = ct.getBean(UserService.class);
		MessageService messageService = ct.getBean(MessageService.class);

	}
}