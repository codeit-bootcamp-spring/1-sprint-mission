package com.sprint.mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
//
//		JCFUserService userService = context.getBean(JCFUserService.class);
//
//		UserDtoForRequest userDtoForRequest = new UserDtoForRequest("userA", "1234", "id");
//		userService.create(userDtoForRequest);

		//UserDtoForRequest updateForm = new UserDtoForRequest("userA-1", "SSS", "icb");
		//userService.update(userA.getId(), updateForm);
	}
}
