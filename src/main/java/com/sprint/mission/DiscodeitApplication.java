package com.sprint.mission;

import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.dto.request.UserDtoForRequest;
import com.sprint.mission.service.jcf.main.JCFUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		JCFUserService userService = context.getBean(JCFUserService.class);

		UserDtoForRequest userDtoForRequest = new UserDtoForRequest("userA", "1234", "id");
		User userA = userService.create(userDtoForRequest);

		UserDtoForRequest updateForm = new UserDtoForRequest("userA-1", "SSS", "icb");
		userService.update(userA.getId(), updateForm);
//		ChannelService channelService = context.getBean(ChannelService.class);
//		MessageService messageService = context.getBean(MessageService.class);
//
//		System.out.println("UserService Bean: " + userService);
//		System.out.println("ChannelService Bean: " + channelService);
//		System.out.println("MessageService Bean: " + messageService);
//		// yml 설정 공부 하기
	}
}
