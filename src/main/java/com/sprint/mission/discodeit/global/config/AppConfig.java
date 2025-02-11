package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;

@Configuration
public class AppConfig {
	// Repositories
	@Bean
	public UserRepository userRepository() {
		return new JCFUserRepository();
	}

	@Bean
	public ChannelRepository channelRepository() {
		return new JCFChannelRepository();
	}

	@Bean
	public MessageRepository messageRepository() {
		return new JCFMessageRepository();
	}

	@Bean
	public UserStatusRepository userStatusRepository() {
		return new JCFUserStatusRepository();
	}

	@Bean
	public ReadStatusRepository readStatusRepository() {
		return new JCFReadStatusRepository();
	}

	@Bean
	public BinaryContentRepository binaryContentRepository() {
		return new JCFBinaryContentRepository();
	}

	// Services
	@Bean
	public UserService userService() {
		return new BasicUserService(
			userRepository(),
			userStatusService(),
			binaryContentService()
		);
	}

	@Bean
	public AuthService authService() {
		return new BasicAuthService(
			userRepository(),
			userStatusRepository()
		);
	}

	@Bean
	public ChannelService channelService() {
		return new BasicChannelService(
			channelRepository(),
			userService(),
			readStatusService(),
			messageRepository(),
			readStatusRepository()
		);
	}

	@Bean
	public MessageService messageService() {
		return new BasicMessageService(
			messageRepository(),
			userService(),
			channelService(),
			binaryContentService()
		);
	}

	@Bean
	public UserStatusService userStatusService() {
		return new BasicUserStatusService(
			userStatusRepository(),
			userRepository()
		);
	}

	@Bean
	public ReadStatusService readStatusService() {
		return new BasicReadStatusService(
			readStatusRepository(),
			userRepository(),
			channelRepository()
		);
	}

	@Bean
	public BinaryContentService binaryContentService() {
		return new BasicBinaryContentService(
			binaryContentRepository(),
			userRepository(),
			messageRepository()
		);
	}
}
