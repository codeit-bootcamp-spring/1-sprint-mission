package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.message.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.channel.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.message.service.BinaryContentService;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.message.service.MessageService;
import com.sprint.mission.discodeit.channel.service.ReadStatusService;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.user.service.UserStatusService;
import com.sprint.mission.discodeit.auth.service.BasicAuthService;
import com.sprint.mission.discodeit.message.service.BasicBinaryContentService;
import com.sprint.mission.discodeit.channel.service.BasicChannelService;
import com.sprint.mission.discodeit.message.service.BasicMessageService;
import com.sprint.mission.discodeit.channel.service.BasicReadStatusService;
import com.sprint.mission.discodeit.user.service.BasicUserService;
import com.sprint.mission.discodeit.user.service.BasicUserStatusService;

@Configuration
public class ServiceConfig {

	@Bean
	public UserService userService(UserRepository userRepository, UserStatusService userStatusService,
		BinaryContentService binaryContentService) {
		return new BasicUserService(userRepository, userStatusService, binaryContentService);
	}

	@Bean
	public AuthService authService(UserRepository userRepository, UserStatusService userStatusService) {
		return new BasicAuthService(userRepository, userStatusService);
	}

	@Bean
	public ChannelService channelService(ChannelRepository channelRepository, UserService userService,
		ReadStatusService readStatusService, UserStatusService userStatusService, MessageRepository messageRepository) {
		return new BasicChannelService(channelRepository, userService, readStatusService, userStatusService,
			messageRepository);
	}

	@Bean
	public MessageService messageService(MessageRepository messageRepository, UserService userService,
		UserStatusService userStatusService,
		ChannelService channelService, BinaryContentService binaryContentService) {
		return new BasicMessageService(messageRepository, userService, userStatusService, channelService,
			binaryContentService);
	}

	@Bean
	public UserStatusService userStatusService(UserStatusRepository userStatusRepository,
		UserRepository userRepository) {
		return new BasicUserStatusService(userStatusRepository, userRepository);
	}

	@Bean
	public ReadStatusService readStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository,
		ChannelRepository channelRepository, MessageRepository messageRepository) {
		return new BasicReadStatusService(readStatusRepository, userRepository, channelRepository, messageRepository);
	}

	@Bean
	public BinaryContentService binaryContentService(BinaryContentRepository binaryContentRepository,
		UserRepository userRepository, MessageRepository messageRepository) {
		return new BasicBinaryContentService(binaryContentRepository, userRepository, messageRepository);
	}
}
