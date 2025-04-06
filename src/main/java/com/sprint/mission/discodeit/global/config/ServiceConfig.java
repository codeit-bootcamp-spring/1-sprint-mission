package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.auth.service.BasicAuthService;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.channel.service.BasicChannelService;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.binaryContent.service.BasicBinaryContentService;
import com.sprint.mission.discodeit.message.service.BasicMessageService;
import com.sprint.mission.discodeit.binaryContent.service.BinaryContentService;
import com.sprint.mission.discodeit.message.service.MessageService;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.readStatus.service.BasicReadStatusService;
import com.sprint.mission.discodeit.readStatus.service.ReadStatusService;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.service.BasicUserService;
import com.sprint.mission.discodeit.user.service.BasicUserStatusService;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.user.service.UserStatusService;

@Configuration
public class ServiceConfig {

	@Bean
	public UserService userService(UserRepository userRepository, UserMapper userMapper,
		BinaryContentRepository binaryContentRepository,
		UserStatusRepository userStatusRepository) {
		return new BasicUserService(userRepository, userMapper, binaryContentRepository, userStatusRepository);
	}

	@Bean
	public AuthService authService(UserRepository userRepository, UserStatusService userStatusService) {
		return new BasicAuthService(userRepository, userStatusService);
	}

	@Bean
	public ChannelService channelService(ChannelRepository channelRepository, ReadStatusRepository readStatusRepository,
		MessageRepository messageRepository, ChannelMapper channelMapper) {
		return new BasicChannelService(channelRepository, readStatusRepository, messageRepository, channelMapper);
	}

	@Bean
	public MessageService messageService(MessageRepository messageRepository, ChannelRepository channelRepository,
		UserRepository userRepository, BinaryContentRepository binaryContentRepository) {
		return new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);
	}

	@Bean
	public UserStatusService userStatusService(UserStatusRepository userStatusRepository,
		UserRepository userRepository) {
		return new BasicUserStatusService(userStatusRepository, userRepository);
	}

	@Bean
	public ReadStatusService readStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository,
		ChannelRepository channelRepository) {
		return new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);
	}

	@Bean
	public BinaryContentService binaryContentService(BinaryContentRepository binaryContentRepository) {
		return new BasicBinaryContentService(binaryContentRepository);
	}
}
