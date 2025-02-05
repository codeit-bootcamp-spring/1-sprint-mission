package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // ✅ 설정 클래스임을 명시
public class RepositoryConfig {

    @Bean  // ✅ FileUserRepository를 Bean으로 등록
    public UserRepository userRepository() {
        return new FileUserRepository();
    }

    @Bean  // ✅ FileChannelRepository를 Bean으로 등록
    public ChannelRepository channelRepository() {
        return new FileChannelRepository();
    }

    @Bean  // ✅ FileMessageRepository를 Bean으로 등록
    public MessageRepository messageRepository() {
        return new FileMessageRepository();
    }
}
