package com.sprint.mission.discodeit.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.message.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.channel.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.message.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.message.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.channel.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserStatusRepository;

@Configuration
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFRepositoryConfig {
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
}
