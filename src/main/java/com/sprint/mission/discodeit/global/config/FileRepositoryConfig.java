package com.sprint.mission.discodeit.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.message.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.channel.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.message.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.message.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.channel.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.user.repository.file.FileUserStatusRepository;

@Configuration
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileRepositoryConfig {

	@Value("${discodeit.repository.file-directory}")
	private String fileDirectory;

	@Bean
	public UserRepository userRepository() {
		return new FileUserRepository(fileDirectory);
	}

	@Bean
	public ChannelRepository channelRepository() {
		return new FileChannelRepository(fileDirectory);
	}

	@Bean
	public MessageRepository messageRepository() {
		return new FileMessageRepository(fileDirectory);
	}

	@Bean
	public UserStatusRepository userStatusRepository() {
		return new FileUserStatusRepository(fileDirectory);
	}

	@Bean
	public ReadStatusRepository readStatusRepository() {
		return new FileReadStatusRepository(fileDirectory);
	}

	@Bean
	public BinaryContentRepository binaryContentRepository() {
		return new FileBinaryContentRepository(fileDirectory);
	}
}
