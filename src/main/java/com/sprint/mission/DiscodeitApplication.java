package com.sprint.mission;

import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
//
//		UserService userService = context.getBean(UserService.class);
//		ChannelService channelService = context.getBean(ChannelService.class);
//		MessageService messageService = context.getBean(MessageService.class);
//
//		System.out.println("UserService Bean: " + userService);
//		System.out.println("ChannelService Bean: " + channelService);
//		System.out.println("MessageService Bean: " + messageService);
//		// yml 설정 공부 하기
	}
}
