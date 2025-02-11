package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.basic.BasicUserService;
import com.sprint.mission.discodeit.basic.BasicChannelService;
import com.sprint.mission.discodeit.basic.BasicMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final boolean useFileStorage = false; // true: File 저장소 사용, false: JCF 저장소 사용

    @Bean
    public UserRepository userRepository() {
        return useFileStorage ? new FileUserRepository() : new JCFUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return useFileStorage ? new FileChannelRepository() : new JCFChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return useFileStorage ? new FileMessageRepository() : new JCFMessageRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return useFileStorage ? new FileBinaryContentRepository() : new JCFBinaryContentRepository();
    }

    @Bean
    public UserService userService(UserRepository userRepository, BinaryContentRepository binaryContentRepository) {
        return new BasicUserService(userRepository, binaryContentRepository);
    }

    @Bean
    public ChannelService channelService(ChannelRepository channelRepository) {
        return new BasicChannelService(channelRepository);
    }

    @Bean
    public MessageService messageService(MessageRepository messageRepository,
                                         ChannelRepository channelRepository,
                                         UserRepository userRepository) {
        return new BasicMessageService(messageRepository, channelRepository, userRepository);
    }
}