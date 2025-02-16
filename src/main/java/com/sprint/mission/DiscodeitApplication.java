package com.sprint.mission;

import com.sprint.mission.entity.main.User;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.service.jcf.main.JCFUserService;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

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
