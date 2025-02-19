package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.channel.CreateChannel;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileIOUtil;
import com.sprint.mission.discodeit.util.type.ChannelFormat;
import com.sprint.mission.discodeit.util.type.ChannelType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
	static UserDTO setupUser(UserService userService) {
		CreateUserRequest createUserRequest = new CreateUserRequest("student1", "010-1292-1188", "Abcdefgh1231!!2", null);
		UserDTO user = userService.create(createUserRequest);
		return user;
	}

	static Channel setupChannel(ChannelService channelService, List<UUID> joinUsersID) {
		CreateChannel.PublicRequest publicRequest = new CreateChannel.PublicRequest("자바 개발자 오픈 채널", "자바 개발자 누구든 환영입니다!", ChannelFormat.TEXT, joinUsersID);
		Channel channel = channelService.createPublicChannel(publicRequest);
		return channel;
	}

	static Message messageCreateTest(MessageService messageService, UUID channelID, UUID writerID) {
		CreateMessageRequest request = new CreateMessageRequest(null, "안녕하세요", writerID, channelID);
		Message message = messageService.create(request);
		System.out.println("메시지 생성 : " + message);
		return message;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		System.out.println("UserRepository 의존성 주입 확인 :  " + context.getBean(UserRepository.class));
		System.out.println("ChannelRepository 의존성 주입 확인 :  " + context.getBean(ChannelRepository.class));
		System.out.println("MessageRepository 의존성 주입 확인 :  " + context.getBean(MessageRepository.class));

		FileIOUtil.initializeFiles();

		UserDTO user = setupUser(userService);
		Channel channel = setupChannel(channelService, List.of(user.getId()));
		messageCreateTest(messageService, user.getId(), channel.getId());

		FileIOUtil.convertSerToJson(Path.of("./result/users.ser"), Path.of("./json/users.json"), User.class);
	}
}