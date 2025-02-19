package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeItApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(DiscodeItApplication.class, args);
		System.out.println("=== DiscodeIt Application 시작 ===");

		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		UserRepository userRepository = context.getBean(UserRepository.class);

		// 1. 사용자 생성 및 저장
		User testUser = new User("testUser", "test@example.com", "password123");
		testUser = userRepository.save(testUser);
		UUID testUserId = testUser.getId();

		System.out.println("사용자 생성 완료: " + testUser.getId());

		// 2. 공용 채널 생성
		PublicChannelCreateRequest publicChannelRequest = new PublicChannelCreateRequest("Public Channel", "A public chat room");
		ChannelResponse publicChannel = channelService.createPublicChannel(publicChannelRequest);
		UUID testChannelId = publicChannel.getId();

		System.out.println("공용 채널 생성 완료: " + publicChannel.getId());

		// 3. 메시지 생성
		MessageCreateRequest messageRequest = new MessageCreateRequest(testChannelId, testUserId, "Hello World!", null);
		MessageResponse messageResponse = messageService.create(messageRequest);

		System.out.println("메시지 생성 완료: " + messageResponse.getContent());


		context.close();
	}
}
