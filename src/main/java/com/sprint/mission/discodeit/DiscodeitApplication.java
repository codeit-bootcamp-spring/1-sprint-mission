package com.sprint.mission.discodeit;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DiscodeitApplication {

	static UserResponse setupUser(UserService userService) {
		CreateUserRequest createUserRequest = new CreateUserRequest("woody", "woody1234", "woody", "woody@codeit.com",
			null); // profileImage (MultipartFile, 없으므로 null)
		return userService.createUser(createUserRequest);
	}

	static Channel setupChannel(ChannelService channelService, UUID participantId) {
		CreatePublicChannelRequest createChannelRequest = new CreatePublicChannelRequest("공지", "공지 채널입니다.",
			List.of(participantId));
		return channelService.createPublicChannel(createChannelRequest);
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		CreateMessageRequest createMessageRequest = new CreateMessageRequest("안녕하세요.", author.getId(), channel.getId(),
			List.of());
		MessageResponse response = messageService.create(createMessageRequest);
		log.info("메시지 생성: " + response.id());
	}

	private static UserService userService;
	private static ChannelService channelService;
	private static MessageService messageService;
	private static UserStatusService userStatusService;
	private static ReadStatusService readStatusService;
	private static BinaryContentService binaryContentService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		userService = context.getBean(UserService.class);
		log.info("userService = " + userService);
		channelService = context.getBean(ChannelService.class);
		log.info("channelService = " + channelService);
		messageService = context.getBean(MessageService.class);
		log.info("messageService = " + messageService);
		userStatusService = context.getBean(UserStatusService.class);
		log.info("userStatusService = " + userStatusService);
		readStatusService = context.getBean(ReadStatusService.class);
		log.info("readStatusService = " + readStatusService);
		binaryContentService = context.getBean(BinaryContentService.class);
		log.info("binaryContentService = " + binaryContentService);

		log.info("setupUser 메서드 시작 :::");
		UserResponse userResponse = setupUser(userService);
		log.info("setupUser 메서드 완료 :::");

		log.info("findUserEntity 메서드 시작 :::");
		User user = userService.findUserEntity(userResponse.id());
		log.info("findUserEntity 메서드 완료 :::");

		log.info("setupChannel 메서드 시작 :::");
		Channel channel = setupChannel(channelService, user.getId());
		log.info("setupChannel 메서드 완료 :::");

		log.info("messageCreateTest 메서드 시작 :::");
		messageCreateTest(messageService, channel, user);
		log.info("messageCreateTest 메서드 완료 :::");
	}
}